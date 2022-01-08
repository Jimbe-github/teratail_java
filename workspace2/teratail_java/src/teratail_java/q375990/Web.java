package teratail_java.q375990;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Web {
  public static void main(String[] args) throws Exception {
    String url = "http://k-ani.com/rss/all.rss";
    List<String> titleList = getTextContentList(readXML(new URL(url)), "//item/title");

    for(int i=0; i<titleList.size(); i++) {
      System.out.println((i+1) + ": " + titleList.get(i));
    }
  }

  static Document readXML(URL url) throws IOException, ParserConfigurationException, SAXException {
    HttpURLConnection con = (HttpURLConnection)url.openConnection();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();

    try(InputStream is = con.getInputStream()) {
      return builder.parse(is);
    }
  }

  static List<String> getTextContentList(Document document, String expression) throws XPathExpressionException {
    XPathExpression expr = XPathFactory.newInstance().newXPath().compile(expression);

    List<String> list = new ArrayList<>();
    NodeList nodeList = (NodeList)expr.evaluate(document, XPathConstants.NODESET);
    for(int i=0; i<nodeList.getLength(); i++) {
      list.add(((Element)nodeList.item(i)).getTextContent());
    }
    return list;
  }
}
