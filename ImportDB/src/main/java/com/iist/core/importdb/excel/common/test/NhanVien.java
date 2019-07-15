package com.iist.core.importdb.excel.common.test;

import java.util.Date;

import com.iist.core.importdb.arr.common.annotation.Column;
import com.iist.core.importdb.arr.common.annotation.Model;
import com.iist.core.importdb.arr.common.annotation.Table;

@Table(id="t_id", name="tbl_NhanVien")
public class NhanVien extends Model {

	@Column(name="maNhanVien")
	public String maNhanVien;

	@Column(name="hoVaTen")
	public String hoVaTen;

	@Column(name="chiNhanh")
	public String chiNhanh;

	@Column(name="khoi")
	public short khoi;

	@Column(name="phong")
	public String phong;

	@Column(name="phanLoaiNhanVien")
	public String phanLoaiNhanVien;

	@Column(name="ngayTuyenDung")
	public Date ngayTuyenDung;

	@Column(name="chucVu")
	public String chucVu;

	@Column(name="chucDanh")
	public String chucDanh;

	@Column(name="chuyenMon")
	public String chuyenMon;

	

	public NhanVien() {
		super();
	}

	public NhanVien(String maNhanVien, String hoVaTen, String chiNhanh, short khoi, String phong,
			String phanLoaiNhanVien, Date ngayTuyenDung, String chucVu, String chucDanh, String chuyenMon) {
		super();
		this.maNhanVien = maNhanVien;
		this.hoVaTen = hoVaTen;
		this.chiNhanh = chiNhanh;
		this.khoi = khoi;
		this.phong = phong;
		this.phanLoaiNhanVien = phanLoaiNhanVien;
		this.ngayTuyenDung = ngayTuyenDung;
		this.chucVu = chucVu;
		this.chucDanh = chucDanh;
		this.chuyenMon = chuyenMon;
	}

}