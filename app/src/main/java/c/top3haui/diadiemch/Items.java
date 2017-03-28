package c.top3haui.diadiemch;

import java.io.Serializable;

/**
 * Created by VanCuong on 28/11/2016.
 */

public class Items implements Serializable {

    private String ten;
    private String loaigas;
    private String motagia;
    private String sdt;
    private String chucuahang;
    private String diadiem;
    private String latlng;

    public Items(String ten, String loaigas, String motagia, String sdt, String chucuahang, String diadiem, String latlng) {
        this.ten = ten;
        this.loaigas = loaigas;
        this.motagia = motagia;
        this.sdt = sdt;
        this.chucuahang = chucuahang;
        this.diadiem = diadiem;
        this.latlng = latlng;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getLoaigas() {
        return loaigas;
    }

    public void setLoaigas(String loaigas) {
        this.loaigas = loaigas;
    }

    public String getMotagia() {
        return motagia;
    }

    public void setMotagia(String motagia) {
        this.motagia = motagia;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getChucuahang() {
        return chucuahang;
    }

    public void setChucuahang(String chucuahang) {
        this.chucuahang = chucuahang;
    }

    public String getDiadiem() {
        return diadiem;
    }

    public void setDiadiem(String diadiem) {
        this.diadiem = diadiem;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }
}
