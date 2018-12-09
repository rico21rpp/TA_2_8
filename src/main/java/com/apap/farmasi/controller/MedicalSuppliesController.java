package com.apap.farmasi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.apap.farmasi.model.JadwalJagaModel;
import com.apap.farmasi.model.JenisMedicalSuppliesModel;
import com.apap.farmasi.model.MedicalSuppliesModel;
import com.apap.farmasi.model.PerencanaanMedicalSuppliesModel;
import com.apap.farmasi.model.PerencanaanModel;
import com.apap.farmasi.model.PermintaanMedicalSuppliesModel;
import com.apap.farmasi.model.PermintaanModel;
import com.apap.farmasi.model.StatusPermintaanModel;
import com.apap.farmasi.repository.MedicalSuppliesDb;
import com.apap.farmasi.rest.BillingDetail;
import com.apap.farmasi.rest.ObatModel;
import com.apap.farmasi.rest.PasienModel;
import com.apap.farmasi.rest.Setting;
import com.apap.farmasi.rest.StaffDetail;
import com.apap.farmasi.service.JadwalService;
import com.apap.farmasi.service.JenisMedicalSuppliesService;
import com.apap.farmasi.service.MedicalSuppliesService;
import com.apap.farmasi.service.PerencanaanService;
import com.apap.farmasi.service.PermintaanService;
import com.apap.farmasi.service.RestService;
import com.apap.farmasi.service.StatusPermintaanService;
//import com.apap.farmasi.model.JadwalJagaModel;

@Controller
@RequestMapping("/medical-supplies")
public class MedicalSuppliesController {

	@Autowired 
	private MedicalSuppliesService medicalSuppliesService;
	
	@Autowired 
	private PermintaanService permintaanService;
	
	@Autowired
	private PerencanaanService perencanaanService;
	
	@Autowired 
	private RestService restService;

	@Autowired
	private StatusPermintaanService statusPermintaanService;
	
	@Autowired 
	private JadwalService jadwalService;

	@Autowired
	RestTemplate restTemplate;
	
	@Bean
	public RestTemplate rest() {
		return new RestTemplate();
	}

	
	
	@Autowired
	private JenisMedicalSuppliesService jenisMedicalSuppliesService;
	
	/**
	 * fitur 3 melihat daftar medical supplies
	 * @param model
	 * @return tampilan daftar seluruh medical supplies
	 */	
	@RequestMapping(value = "", method = RequestMethod.GET)
	private String viewAllDaftarMedicalSupplies(Model model) {
		MedicalSuppliesDb medsupRepo = medicalSuppliesService.viewAllDaftarMedicalSupplies();
		List<MedicalSuppliesModel> allMedSup = medsupRepo.findAll();
		model.addAttribute("allMedSup", allMedSup);
		
		return "view-all-medical-supplies";
	}
	
	/**
	 * fitur 5 melihat detail medical supplies
	 * @param id, model
	 * @return tampilan detail medical supplies
	 */	
	@RequestMapping(value = "/{id}/", method = RequestMethod.GET)
	private String detailMedicalSupplies(@PathVariable (value = "id") long id, Model model) {
		MedicalSuppliesModel medsup = medicalSuppliesService.getMedicalSuppliesDetailById(id);
		model.addAttribute("medsup", medsup);

		return "view-detail-medical-supplies";
	}
	
	/**
	 * fitur 6 menambahkan medical supplies
	 * @param medsup, model
	 * @return tampilan form menambahkan medical supplies
	 */	
	@RequestMapping(value = "/tambah", method = RequestMethod.POST)
	private String addMedsupSubmission(@ModelAttribute MedicalSuppliesModel medsup, Model model) {
		medicalSuppliesService.addMedsup(medsup);
		model.addAttribute("msg", "Berhasil ditambahkan");
		return "success";
	}
	
	@RequestMapping(value = "/tambah", method = RequestMethod.GET)
	private String addMedsup(Model model) {
		model.addAttribute("medsup", new MedicalSuppliesModel());
		List<JenisMedicalSuppliesModel> listJenis = jenisMedicalSuppliesService.getAllJenisMedicalSuppliesById();
		model.addAttribute("listJenis", listJenis);
		return "add-medsup";
	}
	
	/**
	 * fitur 7 mengubah medical supplies
	 * @param medsup, model
	 * @return tampilan form mengubah medical supplies
	 */	
	@RequestMapping(value = "/ubah/{id}/", method = RequestMethod.POST)
	private String updateMedsupSubmission(@PathVariable (value = "id") long id, MedicalSuppliesModel medsup, Model model) {
		medicalSuppliesService.addMedsup(medsup);
		model.addAttribute("msg", "Berhasil ditambahkan");
		return "success";
	}
	
	@RequestMapping(value = "/ubah/{id}/", method = RequestMethod.GET)
	private String updateMedsup(@PathVariable (value = "id") long id, MedicalSuppliesModel medsup, Model model) {
		MedicalSuppliesModel medsup1 = medicalSuppliesService.getMedicalSuppliesDetailById(id);
		model.addAttribute("medsup1", medsup1);
		List<JenisMedicalSuppliesModel> listJenis = jenisMedicalSuppliesService.getAllJenisMedicalSuppliesById();
		model.addAttribute("listJenis", listJenis);
		return "add-medsup";
	}
	
	@RequestMapping(value = "/perencanaan", method = RequestMethod.GET)
	private String viewPerencanaan(Model model) {
		List<PerencanaanModel> listPerencanaan = perencanaanService.getAllPerencanaan();
		
		if (!listPerencanaan.isEmpty()) {
			model.addAttribute("listPerencanaan", listPerencanaan);
			
			PerencanaanModel perencanaan = listPerencanaan.get(0);
			model.addAttribute("aPerencanaan", perencanaan);
			
			List<PerencanaanMedicalSuppliesModel> listPerencanaanMedicalSupplies = perencanaan.getListPerencanaanMedicalSupplies();
			model.addAttribute("listPerencanaanMedSup", listPerencanaanMedicalSupplies);
			
			return "view-perencanaan";
		}
		
		return "perencanaan-kosong";
	}
	
	@RequestMapping(value = "/perencanaan/getPerencanaanById", method = RequestMethod.GET)
	@ResponseBody
	private PerencanaanModel getPerencanaanById(@RequestParam(value = "idPerencanaan", required = true) long idPerencanaan, Model model) {
		return perencanaanService.getPerencanaanDetailById(idPerencanaan).get();
	}
	
	@RequestMapping(value = "/perencanaan/tambah", method = RequestMethod.GET)
	private String tambahPerencanaan(Model model) {
		PerencanaanModel newPerencanaan = new PerencanaanModel();
		model.addAttribute("perencanaan", newPerencanaan);
		return "add-perencanaan";
	}
	
	@RequestMapping(value = "/perencanaan/tambah", method = RequestMethod.POST, params={"addRow"})
	private String addRowPerencanaan(@ModelAttribute PerencanaanModel perencanaan, Model model) {
		PerencanaanMedicalSuppliesModel perencanaanMedsup = new PerencanaanMedicalSuppliesModel();
		List<PerencanaanMedicalSuppliesModel> listPerencanaanMedsup = null;
		
		if (perencanaan.getListPerencanaanMedicalSupplies() != null) {
			listPerencanaanMedsup = perencanaan.getListPerencanaanMedicalSupplies();
		}
		else {
			listPerencanaanMedsup = new ArrayList<PerencanaanMedicalSuppliesModel>();
		}
		listPerencanaanMedsup.add(perencanaanMedsup);
		perencanaan.setListPerencanaanMedicalSupplies(listPerencanaanMedsup);
		model.addAttribute("listPerencanaanMedsup", listPerencanaanMedsup);
		model.addAttribute("perencanaan", perencanaan);
		model.addAttribute("pageTitle", "Add Perencanaan");
		
		return "add-perencanaan";
	}
	
//	@RequestMapping(value = "/perencanaan/tambah", method = RequestMethod.POST)
//	private String tambahPerencanaanSubmit(@ModelAttribute PerencanaanModel perencanaanModel, Model model) {
//		
//	}

	//kerjaan awl
	//fitur 13
	//lebih ribet daripada yg aing bayangin
	@RequestMapping(value = "/permintaan/ubah/{id}", method = RequestMethod.POST)
	private String terimaPermintaan(@PathVariable(value="id") Long id,Model model) {
		
		PermintaanModel targetPermintaan =permintaanService.getPermintaanDetailById(id).get();
		
		List<MedicalSuppliesModel> targetMedSuplst = new ArrayList<MedicalSuppliesModel>();
		int counter = 0;
		//cek cukup ato ga
		for (PermintaanMedicalSuppliesModel temp : targetPermintaan.getListPermintaanMedicalSupplies()) {
			MedicalSuppliesModel medSupIterasi = temp.getMedicalSupplies();
			MedicalSuppliesModel medSupDiDb = medicalSuppliesService.getMedicalSuppliesDetailById(medSupIterasi.getId());
			
			targetMedSuplst.add(medSupIterasi);
			if(targetPermintaan.getJumlahMedicalSupplies() > medSupDiDb.getJumlah()) {
				return "gagal";
			}
			
		}
		//ngurangin di db, bikin billing
		List<BillingDetail> billinglst = new ArrayList<BillingDetail>();
		int jumlahDipesan = (int)targetPermintaan.getJumlahMedicalSupplies();
		for (MedicalSuppliesModel temp : targetMedSuplst) {
			MedicalSuppliesModel medSupDiDb = medicalSuppliesService.getMedicalSuppliesDetailById(temp.getId());
			int jumlahBaru = medSupDiDb.getJumlah()-jumlahDipesan;
			
			medSupDiDb.setJumlah(jumlahBaru);
			
			medicalSuppliesService.addMedsup(medSupDiDb);

			BillingDetail billingTemp = new BillingDetail();
			PasienModel pasienTarget = new PasienModel();
			pasienTarget.setId(targetPermintaan.getIdPasien());
			billingTemp.setPasien(pasienTarget);
			billingTemp.setJumlahTagihan((int)medSupDiDb.getPrice()*jumlahDipesan);
			billingTemp.setTanggalTagihan(targetPermintaan.getTanggal().toString());
			billinglst.add(billingTemp);
		}
		//kirim billing
		RestTemplate rt = rest();
		List<String> responselst = new ArrayList<>();
		int responseCounter = 0;
		String path = Setting.urlApt + "/2/addBilling";
		System.out.println(path);
		for(BillingDetail temp : billinglst) {			
			System.out.print(temp.getJumlahTagihan() + " ");
			System.out.print(temp.getTanggalTagihan().toString() + " ");
			System.out.println(temp.getPasien());

			RestTemplate template = new RestTemplate();
			HttpEntity<BillingDetail> requestEntity= new HttpEntity<BillingDetail>(temp);
			System.out.println(requestEntity.toString());
			String response = "";
		    try{
		       ResponseEntity<String> responseEntity = template.exchange(path, HttpMethod.POST, requestEntity,  String.class);
		       response = responseEntity.getBody();
		    }
		    catch(Exception e){
		       response = e.getMessage();
		    }
		    responselst.add(response);
			System.out.println(response);
			
			
		}

		
		StatusPermintaanModel diterima = statusPermintaanService.getStatusPermintaanDetailById(2);
		targetPermintaan.setStatusPermintaan(diterima);
		permintaanService.addPermintaan(targetPermintaan);
		
		List<StaffDetail> listStaff = restService.getAllStaff().getResult();
		List<PermintaanModel> listPermintaan = permintaanService.getPermintaanList();
		//ditambah awl
		List<StatusPermintaanModel> listStatus = statusPermintaanService.getAllPermintaan();
		model.addAttribute("listStatus",listStatus);
		model.addAttribute("listPermintaan", listPermintaan);
		model.addAttribute("listStaff", listStaff);
		return "viewall-permintaan";
	}
	//fitur 8
	//lebih ribet daripada yg aing bayangin juga
	@RequestMapping(value = "/kirim", method = RequestMethod.POST)
	private String kirimMedSup(@ModelAttribute MedicalSuppliesModel medSup,Model model) {
		
//		System.out.println("hay aul");
		//inisiasi dan kurangin jumlah
		int jumlahDitambah = medSup.getJumlah();
		MedicalSuppliesModel target = medicalSuppliesService.getMedicalSuppliesDetailById(medSup.getId());
		target.setJumlah(target.getJumlah()-jumlahDitambah);
		medicalSuppliesService.addMedsup(target);

		
		
		//kirim ke api sono

		ObatModel obatDikirim = new ObatModel();
		obatDikirim.setJumlah(jumlahDitambah);
		obatDikirim.setNama(target.getNama());
		
		
		String path = Setting.urlMock + "/obat/tambah";

		RestTemplate template = new RestTemplate();
		HttpEntity<ObatModel> requestEntity= new HttpEntity<ObatModel>(obatDikirim);
		System.out.println(requestEntity.toString());
		String response = "";

		System.out.println(path);
		System.out.println("nama = " + obatDikirim.getNama());
		System.out.println("jumlah = " + obatDikirim.getJumlah());

		
		try{
	       ResponseEntity<String> responseEntity = template.exchange(path, HttpMethod.POST, requestEntity,  String.class);
	       response = responseEntity.getBody();
	    }
	    catch(Exception e){
	       response = e.getMessage();
	    }
		
		System.out.println(response);
		
		MedicalSuppliesDb medsupRepo = medicalSuppliesService.viewAllDaftarMedicalSupplies();
		List<MedicalSuppliesModel> allMedSup = medsupRepo.findAll();
		model.addAttribute("allMedSup", allMedSup);
		return "view-all-medical-supplies";
//		return "home";
	}
	
	
	//bukan kerjaan awl lagi
	
//	@RequestMapping(value = "/permintaan", method = RequestMethod.GET)
	@GetMapping(value = "/permintaan")
	private String viewAllPermintaan(Model model) {
		List<StaffDetail> listStaff = restService.getAllStaff().getResult();
		List<PermintaanModel> listPermintaan = permintaanService.getPermintaanList();
		//ditambah awl
		List<StatusPermintaanModel> listStatus = statusPermintaanService.getAllPermintaan();
		model.addAttribute("listStatus",listStatus);
		model.addAttribute("listPermintaan", listPermintaan);
		model.addAttribute("listStaff", listStaff);
		return "viewall-permintaan";
	}

		
	//LIHAT JADWAL JAGA
			@RequestMapping(value = "/jadwal-staf", method = RequestMethod.GET)
			private String viewJadwalJaga(Model model) {
				List<JadwalJagaModel> listJadwalJaga = jadwalService.findAllJadwal();
				List<StaffDetail> listStaff = restService.getAllStaff().getResult();
				model.addAttribute("listJadwalJaga", listJadwalJaga);
				model.addAttribute("listStaff", listStaff);
				return "view-jadwal-jaga";
			}
			
		//TAMBAH JADWAL BARU
			@RequestMapping(value = "/jadwal-staf/tambah", method = RequestMethod.GET)
			private String add(Model model) {
				model.addAttribute("jadwal", new JadwalJagaModel());
				List<StaffDetail> listStaff = restService.getAllStaff().getResult();
				model.addAttribute("listStaff", listStaff);
				return "addNewJadwal";
			}	
			@RequestMapping(value = "/jadwal-staf/tambah", method = RequestMethod.POST)
			private String addNewJadwalSubmit(@ModelAttribute JadwalJagaModel jadwal, Model model) {
				jadwalService.addJadwal(jadwal);
				return "addNewJadwalSuccess";
			}
		
		//UBAH JADWAL  (tidak bisa diubah jika tanggalnya sudah lewat)
		@RequestMapping(value = "/jadwal-staf/update", method = RequestMethod.GET)
		private String updateJadwal(Model model) {
		model.addAttribute("jadwal", new JadwalJagaModel());
		List<StaffDetail> listStaff = restService.getAllStaff().getResult();
		model.addAttribute("listStaff", listStaff);
//		private String updateJadwal(@PathVariable(value = "idJadwal") String idJadwal, Model model) {
//			JadwalJagaModel jadwal = JadwalService.getJadwalDetailById(Long.parseLong(idJadwal));
//			model.addAttribute("jadwal", jadwal);
//			model.addAttribute("newJadwal", new JadwalJagaModel());
			return "updateJadwal";
		}

		@RequestMapping(value = "/jadwal-staf/update", method = RequestMethod.POST)
		private String updateJadwalSubmit(@ModelAttribute JadwalJagaModel jadwal, Model model) {
			jadwalService.addJadwal(jadwal);
//		@RequestMapping(value = "/jadwal-staf/{idJadwaL}", method = RequestMethod.POST)
//		private String updateJadwalSubmit(@ModelAttribute JadwalJagaModel newJadwal, 
//			@PathVariable(value = "idJadwal") String idJadwal, Model model) {
//			JadwalService.updateJadwal(Long.parseLong(idJadwal), newJadwal);
//			model.addAttribute("id", newJadwal.getId());
		return "updateJadwalSuccess";
		}
		
}
