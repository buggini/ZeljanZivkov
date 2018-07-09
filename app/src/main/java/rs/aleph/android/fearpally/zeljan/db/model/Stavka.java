package rs.aleph.android.fearpally.zeljan.db.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Stavka.TABLE_NAME_USERS)
public class Stavka {


    public static final String TABLE_NAME_USERS = "stavke";
    public static final String FIELD_NAME_ID     = "id";
    public static final String TABLE_STAVKA_NASLOV = "naslov";
    public static final String TABLE_STAVKA_OPIS = "opis";
    public static final String TABLE_STAVKA_OZBILJNOST = "ozbiljnost";
    public static final String TABLE_STAVKA_KOMENTAR = "komentar";
    public static final String TABLE_STAVKA_DATUM = "DATUM";
    public static final String FIELD_NAME_USER  = "user";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = TABLE_STAVKA_NASLOV)
    private String sNaslov;

    @DatabaseField(columnName = TABLE_STAVKA_OPIS)
    private String sOpis;

    @DatabaseField(columnName = TABLE_STAVKA_OZBILJNOST)
    private String sOzbiljnost;

    @DatabaseField(columnName = TABLE_STAVKA_KOMENTAR)
    private String sKomentar;

    @DatabaseField(columnName = TABLE_STAVKA_DATUM)
    private String sDatum;

    @DatabaseField(columnName = FIELD_NAME_USER, foreign = true, foreignAutoRefresh = true)
    private Prijava mUser;


    public Stavka(){


    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getsNaslov() {
        return sNaslov;
    }

    public void setsNaslov(String sNaslov) {
        this.sNaslov = sNaslov;
    }

    public String getsOpis() {
        return sOpis;
    }

    public void setsOpis(String sOpis) {
        this.sOpis = sOpis;
    }

    public String getsOzbiljnost() {
        return sOzbiljnost;
    }

    public void setsOzbiljnost(String sOzbiljnost) {
        this.sOzbiljnost = sOzbiljnost;
    }

    public String getsKomentar() {
        return sKomentar;
    }

    public void setsKomentar(String sKomentar) {
        this.sKomentar = sKomentar;
    }

    public String getsDatum() {
        return sDatum;
    }

    public void setsDatum(String sDatum) {
        this.sDatum = sDatum;
    }

    public Prijava getmUser() {
        return mUser;
    }

    public void setmUser(Prijava mUser) {
        this.mUser = mUser;
    }
    @Override
    public String toString() {
        return sNaslov;
    }
}
