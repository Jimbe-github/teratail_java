package teratail_java.q375746;

import java.util.StringJoiner;

class CompactDisc implements ManagedObject {
  private static final String TYPE_NAME = "CD";
  private static final String[] PROPERTY_NAMES = { "タイトル", "アーティスト", "その他情報" };

  static class Factory extends ManagedObjectFactory<CompactDisc> {
    Factory() {
      super(TYPE_NAME, PROPERTY_NAMES);
    }
    @Override
    public CompactDisc create() {
      return new CompactDisc(getProperty(0), getProperty(1), getProperty(2));
    }
  }

  final String title, artist, etcetera;
  private CompactDisc(String title, String artist, String etcetera) {
    this.title = title;
    this.artist = artist;
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
    sj.add(PROPERTY_NAMES[1] + ":" + artist);
    sj.add(PROPERTY_NAMES[2] + ":" + etcetera);
    return sj.toString();
  }
}