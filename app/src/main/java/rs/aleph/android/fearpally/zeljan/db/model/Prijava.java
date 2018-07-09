package rs.aleph.android.fearpally.zeljan.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Prijava.TABLE_NAME_USERS)
public class Prijava {

    public static final String TABLE_NAME_USERS = "prijava";
    public static final String FIELD_NAME_ID     = "id";
    public static final String TABLE_PRIJAVA_NAME = "name";
    public static final String TABLE_PRIJAVA_STATUS = "status";
    public static final String TABLE_PRIJAVA_DATUM = "datum";
    public static final String TABLE_PRIJAVA_OPIS = "opis";
    public static final String TABLE_STAVKA_STAVKE = "stavke";


    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = TABLE_PRIJAVA_NAME)
    private String mName;

    @DatabaseField(columnName = TABLE_PRIJAVA_STATUS)
    private String mStatus;

    @DatabaseField(columnName = TABLE_PRIJAVA_DATUM)
    private String mDatum;

    @DatabaseField(columnName = TABLE_PRIJAVA_OPIS)
    private String mOpis;

    @ForeignCollectionField(columnName = Prijava.TABLE_STAVKA_STAVKE, eager = true)
    private ForeignCollection<Stavka> stavka;


    public Prijava(){

    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmDatum() {
        return mDatum;
    }

    public void setmDatum(String mDatum) {
        this.mDatum = mDatum;
    }

    public String getmOpis() {
        return mOpis;
    }

    public void setmOpis(String mOpis) {
        this.mOpis = mOpis;
    }

    public ForeignCollection<Stavka> getStavka() {
        return stavka;
    }

    public void setStavka(ForeignCollection<Stavka> stavka) {
        this.stavka = stavka;
    }

    @Override
    public String toString() {
        return mName;
    }
}
