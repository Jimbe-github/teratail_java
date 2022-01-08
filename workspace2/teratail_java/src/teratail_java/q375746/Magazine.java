package teratail_java.q375746;

import java.util.StringJoiner;

class Magazine implements ManagedObject {
  private static final String TYPE_NAME = "雑誌";
  private static final String[] PROPERTY_NAMES = { "書名", "出版社", "その他情報" };

  static class Factory extends ManagedObjectFactory<Magazine> {
    Factory() {
      super(TYPE_NAME, PROPERTY_NAMES);
    }
    @Override
    public Magazine create() {
      return new Magazine(getProperty(0), getProperty(1), getProperty(2));
    }
  }

  final String title, publisher, etcetera;
  private Magazine(String title, String publisher, String etcetera) {
    this.title = title;
    this.publisher = publisher;
    this.etcetera = etcetera;
  }

  @Override
  public String toString() {
    return toString(" ");
  }

  @Override
  public String toString(CharSequence delimiter) {
    StringJoiner sj = new StringJoiner(delimiter);
    sj.add("●" + TYPE_NAME);
    sj.add(PROPERTY_NAMES[0] + ":" + title);
    sj.add(PROPERTY_NAMES[1] + ":" + publisher);
    sj.add(PROPERTY_NAMES[2] + ":" + etcetera);
    return sj.toString();
  }
}