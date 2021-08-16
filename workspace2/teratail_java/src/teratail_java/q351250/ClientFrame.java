package teratail_java.q351250;

import java.awt.BorderLayout;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientFrame extends JFrame {
    public static void main(String[] args) throws IOException{
        new ClientFrame().setVisible(true);
    }

    ClientFrame() throws IOException {
        super("テストクライアント");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Socket s = new Socket("localhost", 10140);
        BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
        BufferedInputStream bis = new BufferedInputStream(s.getInputStream());

        JTextArea textArea = new JTextArea(20, 50);
        new Thread() {
            private byte buf[] = new byte[256];
            private int offset = 0;
            public void run() {
                try {
                    while(true) {
                        int n = bis.read(buf, offset, buf.length-offset);
                        offset += n;
                        int index = searchLineSep(buf, offset);
                        if(index >= 0) {
                            String str = new String(buf, 0, index, "sjis");
                            textArea.append(str);
                            textArea.append("\n");
                            offset -= index + 2; //改行は消す
                            System.arraycopy(buf, index+2, buf, 0, offset);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            private int searchLineSep(byte buf[], int length) {
                for(int i=0; i<length-1; i++) {
                    if(buf[i] == '\r' && buf[i+1] == '\n') return i;
                }
                return -1;
            }
        }.start();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JTextField textField = new JTextField(50);
        JButton sendButton = new JButton("send");
        sendButton.addActionListener(l -> {
            try {
                bos.write(textField.getText().getBytes("sjis"));
                bos.write(new byte[] {'\r','\n'});
                bos.flush();
                textField.setText("");
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
        JPanel sendPanel = new JPanel(new BorderLayout());
        sendPanel.add(textField, BorderLayout.CENTER);
        sendPanel.add(sendButton, BorderLayout.EAST);
        add(sendPanel, BorderLayout.SOUTH);

        pack();
    }
}
