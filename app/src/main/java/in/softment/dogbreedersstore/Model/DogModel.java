package in.softment.dogbreedersstore.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class DogModel implements Serializable {

    public String dogType = "";
    public String microChipNumber = "";
    public String ageType = "";
    public int dogAge = 0;
    public int dogPrice = 0;
    public String aboutDog = "";
    public ArrayList<String> dogImages = new ArrayList<>();
    public Date date = new Date();
    public String id = "";
    public String uid = "";
    public String geohash = "";
    public Double lat = 0.0;
    public Double lng = 0.0;
    public String sourcenumber = "";
    public String pedigreepapersire = "";
    public String pedigreepaperdam = "";
    public String vetcheck = "";
    public String vaccinationrecord = "";
    public String pupvideo = "";

    public String getDogType() {
        return dogType;
    }

    public void setDogType(String dogType) {
        this.dogType = dogType;
    }

    public String getMicroChipNumber() {
        return microChipNumber;
    }

    public void setMicroChipNumber(String microChipNumber) {
        this.microChipNumber = microChipNumber;
    }

    public String getAgeType() {
        return ageType;
    }

    public void setAgeType(String ageType) {
        this.ageType = ageType;
    }

    public int getDogAge() {
        return dogAge;
    }

    public void setDogAge(int dogAge) {
        this.dogAge = dogAge;
    }

    public int getDogPrice() {
        return dogPrice;
    }

    public void setDogPrice(int dogPrice) {
        this.dogPrice = dogPrice;
    }

    public String getAboutDog() {
        return aboutDog;
    }

    public void setAboutDog(String aboutDog) {
        this.aboutDog = aboutDog;
    }

    public ArrayList<String> getDogImages() {
        return dogImages;
    }

    public void setDogImages(ArrayList<String> dogImages) {
        this.dogImages = dogImages;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getSourcenumber() {
        return sourcenumber;
    }

    public void setSourcenumber(String sourcenumber) {
        this.sourcenumber = sourcenumber;
    }

    public String getPedigreepapersire() {
        return pedigreepapersire;
    }

    public void setPedigreepapersire(String pedigreepapersire) {
        this.pedigreepapersire = pedigreepapersire;
    }

    public String getPedigreepaperdam() {
        return pedigreepaperdam;
    }

    public void setPedigreepaperdam(String pedigreepaperdam) {
        this.pedigreepaperdam = pedigreepaperdam;
    }

    public String getVetcheck() {
        return vetcheck;
    }

    public void setVetcheck(String vetcheck) {
        this.vetcheck = vetcheck;
    }

    public String getVaccinationrecord() {
        return vaccinationrecord;
    }

    public void setVaccinationrecord(String vaccinationrecord) {
        this.vaccinationrecord = vaccinationrecord;
    }

    public String getPupvideo() {
        return pupvideo;
    }

    public void setPupvideo(String pupvideo) {
        this.pupvideo = pupvideo;
    }
}
