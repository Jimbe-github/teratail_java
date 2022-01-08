package teratail_java.q370289;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

class Queue<T> {
  private Object[] buf;
  private int messagein = 0;
  int start = 0;

  public Queue(int size) {
    // 指定されたサイズでバッファを確保
    buf = new Object[size];
  }
  public Queue() {
    this(10);
  }

  public synchronized void writeMessage(T message) throws InterruptedException {
    // すでにメッセージがある場合
    while (messagein >= buf.length) {
      // notifyされるまで待機
      wait();
    }
    int end = (start + messagein) % buf.length;
    buf[end] = message;
    // メッセージが入った
    messagein++;
    notifyAll();
  }

  public synchronized T readMessage() throws InterruptedException {
    // メッセージが入るまで待つ
    while (messagein == 0) {
      // notifyされるまで待機
      wait();
    }

    @SuppressWarnings("unchecked")
    T message = (T)buf[start];

    start = (start + 1) % buf.length;
    // メッセージがなくなった
    messagein--;
    notifyAll();
    return message;
  }
}

class Message {
  static final int CODE_INVITE = 0;
  static final int CODE_UNREGISTER = -999;

  final String src;
  final String dst;
  final int code;
  final String text;
  Message(String src, String dst, int code) {
    this(src, dst, code, null);
  }
  Message(String src, String dst, int code, String text) {
    this.src = src;
    this.dst = dst;
    this.code = code;

    if(text == null) {
      switch(code) {
        default: text = "";
        case CODE_INVITE: text = "INVITE"; break;
        case CODE_UNREGISTER: text = "UNREGISTER"; break;
      }
    }
    this.text = text;
  }
  public String getTextWithCode() {
    return (code<=0?"":code+" ")+text;
  }
  @Override
  public String toString() {
    return "src='"+src+"', dst='"+dst+"', code="+code+", text='"+text+"'";
  }
}

class Proxy implements Runnable {
  static final String name = "Proxy";

  private Queue<Message> accept = new Queue<Message>();
  private Map<String,Queue<Message>> map = new HashMap<>(); //名前とキューの対応

  public void run() {
    try {
      while(true) {
        Message msg = accept.readMessage();
        if(msg.dst.equals(name)) {
          if(msg.code == Message.CODE_UNREGISTER) {
            map.remove(msg.src);
            if(map.isEmpty()) break; //利用者が居なくなったら終了
          }
        } else {
          if(msg.code == Message.CODE_INVITE) {
            send(new Message(name, msg.src, 100, "Trying"));
          }
          getQueue(msg.dst).writeMessage(msg);
        }
      }
    } catch (InterruptedException e) {
      e.printStackTrace(System.err);
    }
  }

  private Queue<Message> getQueue(String name) {
    synchronized(map) {
      Queue<Message> q = map.get(name);
      if(q == null) {
        q = new Queue<Message>();
        map.put(name, q);
      }
      return q;
    }
  }

  public void send(Message msg) throws InterruptedException {
    if(!msg.src.equals(name) && !msg.dst.equals(name)) { //Proxy[から|へ]は表示しない
      printLog(msg.src + " send: " + msg.getTextWithCode());
    }
    accept.writeMessage(msg);
  }

  public Message recv(String name) throws InterruptedException {
    Message msg = getQueue(name).readMessage();
    printLog(name + " recv: " + msg.getTextWithCode());
    return msg;
  }

  private void printLog(String log) {
    String time = DateTimeFormatter.ofPattern("HH:mm:ss ").format(LocalDateTime.now());
    System.out.println(time + log);
  }
}

class Asan implements Runnable {
  static final String name = "Aさん";

  private Proxy proxy;

  Asan(Proxy proxy) {
    this.proxy = proxy;
  }

  public void run() {
    try {
      proxy.send(new Message(name, Bsan.name, Message.CODE_INVITE));
      Thread.sleep(1000);
      proxy.recv(name); //Trying?
      Thread.sleep(1000);
      proxy.recv(name); //Ringing?
      Thread.sleep(5000);
      proxy.recv(name); //OK?

      proxy.send(new Message(name, Proxy.name, Message.CODE_UNREGISTER));
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }
}

class Bsan implements Runnable {
  static final String name = "Bさん";

  private Proxy proxy;

  Bsan(Proxy proxy) {
    this.proxy = proxy;
  }

  public void run() {
    try {
      Thread.sleep(1000);
      Message recvMsg = proxy.recv(name); //INVITE?
      Thread.sleep(1000);
      proxy.send(new Message(name, recvMsg.src, 180, "Ringing"));
      Thread.sleep(3000);
      proxy.send(new Message(name, recvMsg.src, 200, "OK"));

      proxy.send(new Message(name, Proxy.name, Message.CODE_UNREGISTER));
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }
}

public class Main {
  public static void main(String args[]) throws InterruptedException {
    Proxy proxy = new Proxy();
    Asan sy1 = new Asan(proxy);
    Bsan sy2 = new Bsan(proxy);

    Thread threads[] = { new Thread(proxy), new Thread(sy1), new Thread(sy2) };

    for(Thread th : threads) th.start();
    for(Thread th : threads) th.join();
  }
}