package teratail_java.q363713;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Q363771 {
  public static void main(String[] args) throws Exception {
    List<Book> bookList = getBookList("9784295010517");
    for(Book b: bookList) System.out.println(b); //確認
  }

  static List<Book> getBookList(String isbn) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    //factory.setNamespaceAware(true);
    DocumentBuilder builder = factory.newDocumentBuilder();

    Document document = getDocument(isbn, builder);

    XPath xpath = XPathFactory.newInstance().newXPath();

    XPathExpression recordDataExpr = xpath.compile("//record/recordData/text()");
    NodeList recordDataList = (NodeList)recordDataExpr.evaluate(document, XPathConstants.NODESET);

    List<Book> bookList = new ArrayList<>();

    XPathExpression titleExpr = xpath.compile("//title/text()");
    XPathExpression creatorExpr = xpath.compile("//creator/text()");
    XPathExpression publisherExpr = xpath.compile("//publisher/text()");

    for(int i=0; i<recordDataList.getLength(); i++) {
      String data = recordDataList.item(i).getTextContent();
      Document datadoc = builder.parse(new ByteArrayInputStream(data.getBytes("UTF-8")));

      String title = (String)titleExpr.evaluate(datadoc, XPathConstants.STRING);
      String creator = (String)creatorExpr.evaluate(datadoc, XPathConstants.STRING);
      String publisher = (String)publisherExpr.evaluate(datadoc, XPathConstants.STRING);
      bookList.add(new Book(title, creator, publisher));
    }

    return bookList;
  }

  static Document getDocument(String isbn, DocumentBuilder builder) throws IOException, SAXException {
    String operation = "searchRetrieve";
    String query = URLEncoder.encode("isbn=\""+isbn+"\"", "UTF-8");
    URL url = new URL("https://iss.ndl.go.jp/api/sru?operation="+operation+"&query="+query);
    HttpsURLConnection https = (HttpsURLConnection)url.openConnection();
    https.setRequestMethod("GET");
    https.connect();
    try {
      return builder.parse(https.getInputStream());
    } finally {
      https.disconnect();
    }
  }

  static class Book {
    final String title, creator, publisher;
    Book(String title, String creator, String publisher) {
      this.title = title;
      this.creator = creator;
      this.publisher = publisher;
    }
    @Override
    public String toString() {
      return new StringBuilder("Book")
          .append("[title='").append(title).append("'")
          .append(",creator='").append(creator).append("'")
          .append(",publisher='").append(publisher).append("'")
          .append("]").toString();
    }
  }
}
