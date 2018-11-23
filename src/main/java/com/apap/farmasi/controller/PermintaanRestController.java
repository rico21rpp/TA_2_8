package com.apap.farmasi.controller;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.apap.farmasi.model.JadwalJagaModel;
import com.apap.farmasi.model.PermintaanModel;
import com.apap.farmasi.repository.JadwalJagaDb;
import com.apap.farmasi.repository.PermintaanDb;
import com.apap.farmasi.repository.StatusPermintaanDb;
import com.apap.farmasi.rest.BaseResponse;
import com.apap.farmasi.service.PermintaanService;

@RestController
public class PermintaanRestController {
	
	
	@Autowired
	private PermintaanService permintaanService;
	
	@Autowired
	private StatusPermintaanDb statusPermintaanDb;
	
	@Autowired
	private JadwalJagaDb jadwalJagaDb;
	
	
	@Autowired
	private PermintaanDb permintaanDb;
	@PostMapping(value="/api/medical-supplies/permintaan")
	private BaseResponse<PermintaanModel> addPermintaanSubmit(
												@RequestBody PermintaanModel permintaan,
												BindingResult bindingresult) {
		BaseResponse<PermintaanModel> response = new BaseResponse<PermintaanModel>();

		//date func
		DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
		Calendar today = Calendar.getInstance();
		df.setTimeZone(today.getTimeZone());
		Date tanggalMasuk = new Date(today.getTimeInMillis()); 
		String todayStr = df.format(today.getTime());
		String[] todaySplit = todayStr.split("-");
		String todayTahun = todaySplit[0];
		String todayBulan = todaySplit[1];
		String todayTanggal = todaySplit[2];
		
		//jadwal func
		int jamMasuk = today.get(Calendar.HOUR);
		
		List<JadwalJagaModel> allJadwal = jadwalJagaDb.findAll();
//		System.out.println(allJadwal);
		JadwalJagaModel jadwalDipakai = null;

		//cari tanggal hari ini
		for (JadwalJagaModel jadwalTemp : allJadwal) {
			Date tempJadwalDate = jadwalTemp.getTanggal();
			Time waktuJadwalMulai = jadwalTemp.getWaktuMulai();
			
			String waktuJadwalMulaiStr = waktuJadwalMulai.toString();
			waktuJadwalMulaiStr = waktuJadwalMulaiStr.substring(0,2);
//			System.out.println(waktuJadwalMulaiStr);			
			int waktuJadwalMulaiInt = Integer.parseInt(waktuJadwalMulaiStr);

			String jadwalDateStr = df.format(tempJadwalDate);
			String[] splitJadwalDateStr = jadwalDateStr.split("-");
			String jadwalDateTahun = splitJadwalDateStr[0];
			String jadwalDateBulan = splitJadwalDateStr[1];
			String jadwalDateTanggal = splitJadwalDateStr[2];
			
			
			Calendar tempJadwalCal = Calendar.getInstance();
			tempJadwalCal.setTime(tempJadwalDate);
			Calendar tempJadwalMulai = Calendar.getInstance();
			tempJadwalCal.setTime(waktuJadwalMulai);

//			System.out.println(today);
//			System.out.println(tempJadwalCal);
//			System.out.println("-----------------------------------");
//			System.out.println("jadwalid =" + jadwalTemp.getId());
//			System.out.println(today.get(Calendar.DATE) +"=" + tempJadwalCal.get(Calendar.DATE));
//			System.out.println(today.get(Calendar.MONTH) +"=" + tempJadwalCal.get(Calendar.MONTH));
//			System.out.println(today.get(Calendar.YEAR) +"=" + tempJadwalCal.get(Calendar.YEAR));
//			System.out.println("-----------------------------------");
//			System.out.println(jadwalDateStr);
//			System.out.println(tempJadwalDate);
//			System.out.println(jadwalDateTanggal);
//			System.out.println(jadwalDateBulan);
//			System.out.println(jadwalDateTahun);
//			System.out.println("-----------------------------------");
//			System.out.println(todayTanggal + "=" +jadwalDateTanggal);
//			System.out.println(todayBulan + "=" +jadwalDateBulan);
//			System.out.println(todayTahun + "=" +jadwalDateTahun);
			
			if (todayTanggal.equals(jadwalDateTanggal) &&
				todayBulan.equals(jadwalDateBulan) &&
				todayTahun.equals(jadwalDateTahun)){
					
//				System.out.println("MASOOOOOOOOOOOOK");
//				System.out.println(today.get(Calendar.HOUR_OF_DAY));
//				System.out.println(waktuJadwalMulaiInt);
				if(today.get(Calendar.HOUR_OF_DAY)>=0 && 
				   today.get(Calendar.HOUR_OF_DAY)<16 &&
				    waktuJadwalMulaiInt == 8) {
					System.out.println("masukakhir");
					jadwalDipakai = jadwalTemp;
				}
				else if(today.get(Calendar.HOUR_OF_DAY)>=16 && 
					today.get(Calendar.HOUR_OF_DAY)<=24 &&
					waktuJadwalMulaiInt == 16) {
					jadwalDipakai = jadwalTemp;
					System.out.println("masukakhir");	
				}
			}
			
			
			
//			if (today.get(Calendar.DATE) == tempJadwalCal.get(Calendar.DATE) &&
//				today.get(Calendar.MONTH) == tempJadwalCal.get(Calendar.MONTH) &&
//				today.get(Calendar.YEAR) == tempJadwalCal.get(Calendar.YEAR)) {
//				
//				if(today.get(Calendar.HOUR_OF_DAY)>=0 && 
//				   today.get(Calendar.HOUR_OF_DAY)<16 &&
//				   tempJadwalMulai.get(Calendar.HOUR_OF_DAY) == 8) {
//					jadwalDipakai = jadwalTemp;
//					}
//				else if(today.get(Calendar.HOUR_OF_DAY)>=16 && 
//						today.get(Calendar.HOUR_OF_DAY)<=11 &&
//						tempJadwalMulai.get(Calendar.HOUR_OF_DAY) == 16) {
//					jadwalDipakai = jadwalTemp;
//					
//				}
//			}
		}
		
//		System.out.println("masuk" + jadwalDipakai.getId());
		permintaan.setJadwalJaga(jadwalDipakai);
		permintaan.setTanggal(tanggalMasuk);
		permintaan.setStatusPermintaan(statusPermintaanDb.findById(1).get());
		permintaanDb.save(permintaan);
		
		response.setStatus(200);
		response.setMessage("success");	
		response.setResult(permintaan);
		return response;
	}
	

}
