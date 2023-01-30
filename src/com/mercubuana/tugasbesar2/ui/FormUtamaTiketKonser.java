package com.mercubuana.tugasbesar2.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.border.LineBorder;

import org.orm.PersistentException;
import org.orm.PersistentTransaction;

import com.mercubuana.tugasbesar2.Konser;
import com.mercubuana.tugasbesar2.KonserDAO;
import com.mercubuana.tugasbesar2.Pemesan;
import com.mercubuana.tugasbesar2.PemesanDAO;
import com.mercubuana.tugasbesar2.TugasBesar02PersistentManager;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.JulianFields;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.transaction.Transaction;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class FormUtamaTiketKonser {

	private JFrame frmTiketKonser;
	private JTextField txtNamaKonser;
	private JTextField txtJumlahTiket;
	private JTextField txtSisaTiket;
	private JPanel panelRincianDataKonser;
	private JButton btnTambahData;
	private JList listDaftarKonser;
	private JComboBox cmbJenisTiket;
	private Konser[] dataKonser = null;
	private JButton btnRekamDataKonser;
	private JScrollPane scrollPane;
	private JButton btnHapus;
	private JButton btnUbahData;
	private JButton btnBatal;
	private JButton btnRekamPerubahan;
	private boolean apakahTambahData = true;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private Konser[] dataKonserSesuai = new Konser[0];
	private JRadioButton rdbtnFestival;
	private JRadioButton rdbtnVip;
	private JRadioButton rdbtnTribun;
	private JButton btnCariKonser;
	private JLabel lblDaftarKonserSesuai;
	private JList listDaftarKonserSesuai;
	private JLabel lblNamaKonserDipilih;
	private JLabel lblJumlahTiketPanelDua;
	private JLabel lblSisaTiketPanelDua;
	private JTextField txtNamaKonserPanelDua;
	private JTextField txtJumlahTiketPanelDua;
	private JTextField txtSisaTiketPanelDua;
	private JPanel panelRincianDataPemesan;
	private JLabel lblNamaPemesan;
	private JLabel lblTanggalPemesanan;
	private JTextField txtNamaPemesan;
	private JTextField txtTanggalPemesananPanelDua;
	private JButton btnRekamPemesanan;
	private JList listDaftarKonserPanelTiga;
	private JLabel lblNewLabel;
	private JList listDaftarPemesanPanelTiga;
	private JScrollPane scrollPane_2;
	private JPanel panelTiga;
	private JLabel lblJenisTiketPanelTiga;
	private JLabel lblTanggalPemesananPanelTiga;
	private JTextField txtTanggalPemesananPanelTiga;
	private JComboBox cmbJenisTiketPanelTiga;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FormUtamaTiketKonser window = new FormUtamaTiketKonser();
					window.frmTiketKonser.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FormUtamaTiketKonser() {
		// 1. Inisialiasi dan menampilkan form utama
		initialize();
		/* 2. Membaca data dari database */
		bacaDataDariDatabase();
		/* 3. Memeriksa jika sudah ada data untuk
		 * 	  ditampilkan pada list
		 */
		if (dataKonser.length>0) {
			tampilkanDataKonserPadaList();
			// 4. Pilih konser teratas pada list
			listDaftarKonser.setSelectedIndex(0);
			/* 5. Tampilkan data konser pada list yang ada 
			 *    pada panel tiga
			 */
			listDaftarKonserPanelTiga.setListData(dataKonser);
		}
		nonAktifkanPanelRincian();
//		tampilkanDataKonserPadaList();
//		pilihSebuahKonserPadaList(0);	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTiketKonser = new JFrame();
		frmTiketKonser.setTitle("Tiket Konser");
		frmTiketKonser.setBounds(100, 100, 650, 545);
		frmTiketKonser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTiketKonser.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 632, 498);
		frmTiketKonser.getContentPane().add(tabbedPane);
		
		JPanel panelSatu = new JPanel();
		tabbedPane.addTab("Data Konser", null, panelSatu, null);
		panelSatu.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 40, 396, 146);
		panelSatu.add(scrollPane);
		
		listDaftarKonser = new JList();
		scrollPane.setViewportView(listDaftarKonser);
		listDaftarKonser.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				ubahTampilanPadaPanelRincian();
			}
		});
		listDaftarKonser.setFont(new Font("Dialog", Font.BOLD, 14));
		listDaftarKonser.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JLabel lblDaftarKonser = new JLabel("Daftar Konser");
		lblDaftarKonser.setBounds(12, 12, 113, 16);
		panelSatu.add(lblDaftarKonser);
		lblDaftarKonser.setFont(new Font("Dialog", Font.BOLD, 16));
		
		btnTambahData = new JButton("Tambah Data");
		btnTambahData.setBounds(420, 40, 123, 26);
		panelSatu.add(btnTambahData);
		btnTambahData.setFont(new Font("Dialog", Font.BOLD, 14));
		
		panelRincianDataKonser = new JPanel();
		panelRincianDataKonser.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Rincian Data Konser", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelRincianDataKonser.setName("");
		panelRincianDataKonser.setBounds(12, 199, 507, 177);
		panelSatu.add(panelRincianDataKonser);
		panelRincianDataKonser.setLayout(null);
		
		JLabel lblNamaKonser = new JLabel("Nama Konser : ");
		lblNamaKonser.setBounds(12, 27, 123, 16);
		panelRincianDataKonser.add(lblNamaKonser);
		lblNamaKonser.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNamaKonser.setFont(new Font("Dialog", Font.BOLD, 16));
		
		JLabel lblJenisTiket = new JLabel("Jenis Tiket : ");
		lblJenisTiket.setBounds(12, 55, 123, 16);
		panelRincianDataKonser.add(lblJenisTiket);
		lblJenisTiket.setHorizontalAlignment(SwingConstants.RIGHT);
		lblJenisTiket.setFont(new Font("Dialog", Font.BOLD, 16));
		
		JLabel lblJumlahTiket = new JLabel("Jumlah Tiket : ");
		lblJumlahTiket.setBounds(12, 83, 123, 16);
		panelRincianDataKonser.add(lblJumlahTiket);
		lblJumlahTiket.setHorizontalAlignment(SwingConstants.RIGHT);
		lblJumlahTiket.setFont(new Font("Dialog", Font.BOLD, 16));
		
		JLabel lblSisaTiket = new JLabel("Sisa Tiket : ");
		lblSisaTiket.setBounds(12, 111, 123, 16);
		panelRincianDataKonser.add(lblSisaTiket);
		lblSisaTiket.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSisaTiket.setFont(new Font("Dialog", Font.BOLD, 16));
		
		txtNamaKonser = new JTextField();
		txtNamaKonser.setBounds(139, 27, 150, 20);
		panelRincianDataKonser.add(txtNamaKonser);
		txtNamaKonser.setColumns(10);
		
		txtJumlahTiket = new JTextField();
		txtJumlahTiket.setBounds(139, 83, 60, 20);
		panelRincianDataKonser.add(txtJumlahTiket);
		txtJumlahTiket.setColumns(10);
		
		txtSisaTiket = new JTextField();
		txtSisaTiket.setBounds(139, 111, 60, 20);
		panelRincianDataKonser.add(txtSisaTiket);
		txtSisaTiket.setColumns(10);
		
		cmbJenisTiket = new JComboBox();
		cmbJenisTiket.setBounds(139, 53, 150, 25);
		panelRincianDataKonser.add(cmbJenisTiket);
		cmbJenisTiket.setModel(new DefaultComboBoxModel(new String[] {"Festival", "Tribun", "VIP"}));
		
		btnRekamDataKonser = new JButton("Rekam Data Konser");
		btnRekamDataKonser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rekamDataKonser();
				//rekamDataKonser();
			}
		});
		btnRekamDataKonser.setFont(new Font("Dialog", Font.BOLD, 14));
		btnRekamDataKonser.setBounds(12, 139, 175, 26);
		panelRincianDataKonser.add(btnRekamDataKonser);
		
		btnBatal = new JButton("Batal");
		btnBatal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				masukModeBrowsing();
//				batalkanProsesManipulasiData();
			}
		});
		btnBatal.setFont(new Font("Dialog", Font.BOLD, 14));
		btnBatal.setBounds(372, 139, 123, 26);
		panelRincianDataKonser.add(btnBatal);
		
		btnRekamPerubahan = new JButton("Rekam Perubahan");
		btnRekamPerubahan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rekamPerubahan();
//				masukModeEditing();
//				btnRekamPerubahan.setEnabled(true);
			}
		});
		btnRekamPerubahan.setFont(new Font("Dialog", Font.BOLD, 14));
		btnRekamPerubahan.setBounds(199, 139, 161, 26);
		panelRincianDataKonser.add(btnRekamPerubahan);
		
		btnHapus = new JButton("Hapus");
		btnHapus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				hapusData();
			}
		});
		btnHapus.setFont(new Font("Dialog", Font.BOLD, 14));
		btnHapus.setBounds(420, 160, 123, 26);
		panelSatu.add(btnHapus);
		
		btnUbahData = new JButton("Ubah Data");
		btnUbahData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				apakahTambahData = false;
				masukModeEditing();
				btnRekamPerubahan.setEnabled(true);
				btnRekamDataKonser.setEnabled(false);
			}
		});
		btnUbahData.setFont(new Font("Dialog", Font.BOLD, 14));
		btnUbahData.setBounds(420, 78, 123, 26);
		panelSatu.add(btnUbahData);
		
		JPanel panelDua = new JPanel();
		tabbedPane.addTab("Pemesanan Tiket", null, panelDua, null);
		panelDua.setLayout(null);
		
		JLabel lblPilihJenisTiket = new JLabel("Pilih Jenis Tiket ");
		lblPilihJenisTiket.setFont(new Font("Dialog", Font.BOLD, 14));
		lblPilihJenisTiket.setBounds(12, 12, 129, 16);
		panelDua.add(lblPilihJenisTiket);
		
		rdbtnFestival = new JRadioButton("Festival");
		rdbtnFestival.setFont(new Font("Dialog", Font.BOLD, 14));
		rdbtnFestival.setSelected(true);
		buttonGroup.add(rdbtnFestival);
		rdbtnFestival.setBounds(136, 8, 89, 24);
		panelDua.add(rdbtnFestival);
		
		rdbtnVip = new JRadioButton("VIP");
		rdbtnVip.setFont(new Font("Dialog", Font.BOLD, 14));
		buttonGroup.add(rdbtnVip);
		rdbtnVip.setBounds(136, 64, 89, 24);
		panelDua.add(rdbtnVip);
		
		rdbtnTribun = new JRadioButton("Tribun");
		rdbtnTribun.setFont(new Font("Dialog", Font.BOLD, 14));
		buttonGroup.add(rdbtnTribun);
		rdbtnTribun.setBounds(136, 36, 89, 24);
		panelDua.add(rdbtnTribun);
		
		btnCariKonser = new JButton("Cari Konser");
		btnCariKonser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cariKonser();
			}
		});
		btnCariKonser.setBounds(233, 36, 121, 26);
		panelDua.add(btnCariKonser);
		
		lblDaftarKonserSesuai = new JLabel("Daftar Konser Sesuai :");
		lblDaftarKonserSesuai.setFont(new Font("Dialog", Font.BOLD, 14));
		lblDaftarKonserSesuai.setBounds(12, 96, 162, 16);
		panelDua.add(lblDaftarKonserSesuai);
		
		listDaftarKonserSesuai = new JList();
		listDaftarKonserSesuai.setFont(new Font("Dialog", Font.BOLD, 14));
		listDaftarKonserSesuai.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				ubahRincianDataKonserDipilih();
			}
		});
		listDaftarKonserSesuai.setBorder(new LineBorder(new Color(0, 0, 0)));
		listDaftarKonserSesuai.setBounds(12, 120, 430, 112);
		panelDua.add(listDaftarKonserSesuai);
		
		lblNamaKonserDipilih = new JLabel("Nama Konser Dipilih");
		lblNamaKonserDipilih.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNamaKonserDipilih.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNamaKonserDipilih.setBounds(22, 244, 152, 16);
		panelDua.add(lblNamaKonserDipilih);
		
		lblJumlahTiketPanelDua = new JLabel("Jumlah Tiket");
		lblJumlahTiketPanelDua.setHorizontalAlignment(SwingConstants.RIGHT);
		lblJumlahTiketPanelDua.setFont(new Font("Dialog", Font.BOLD, 14));
		lblJumlahTiketPanelDua.setBounds(22, 272, 152, 16);
		panelDua.add(lblJumlahTiketPanelDua);
		
		lblSisaTiketPanelDua = new JLabel("Sisa Tiket");
		lblSisaTiketPanelDua.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSisaTiketPanelDua.setFont(new Font("Dialog", Font.BOLD, 14));
		lblSisaTiketPanelDua.setBounds(22, 299, 152, 16);
		panelDua.add(lblSisaTiketPanelDua);
		
		txtNamaKonserPanelDua = new JTextField();
		txtNamaKonserPanelDua.setBounds(192, 243, 114, 20);
		panelDua.add(txtNamaKonserPanelDua);
		txtNamaKonserPanelDua.setColumns(10);
		
		txtJumlahTiketPanelDua = new JTextField();
		txtJumlahTiketPanelDua.setBounds(192, 271, 57, 20);
		panelDua.add(txtJumlahTiketPanelDua);
		txtJumlahTiketPanelDua.setColumns(10);
		
		txtSisaTiketPanelDua = new JTextField();
		txtSisaTiketPanelDua.setBounds(192, 298, 57, 20);
		panelDua.add(txtSisaTiketPanelDua);
		txtSisaTiketPanelDua.setColumns(10);
		
		panelRincianDataPemesan = new JPanel();
		panelRincianDataPemesan.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
			}
		});
		panelRincianDataPemesan.setBorder(new TitledBorder(null, "Rincian Data Pemesan", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelRincianDataPemesan.setBounds(12, 340, 430, 118);
		panelDua.add(panelRincianDataPemesan);
		panelRincianDataPemesan.setLayout(null);
		
		lblNamaPemesan = new JLabel("Nama Pemesan");
		lblNamaPemesan.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNamaPemesan.setBounds(12, 45, 120, 16);
		panelRincianDataPemesan.add(lblNamaPemesan);
		
		lblTanggalPemesanan = new JLabel("Tanggal Pemesanan");
		lblTanggalPemesanan.setFont(new Font("Dialog", Font.BOLD, 14));
		lblTanggalPemesanan.setBounds(12, 72, 143, 16);
		panelRincianDataPemesan.add(lblTanggalPemesanan);
		
		txtNamaPemesan = new JTextField();
		txtNamaPemesan.setBounds(173, 44, 245, 20);
		panelRincianDataPemesan.add(txtNamaPemesan);
		txtNamaPemesan.setColumns(10);
		
		txtTanggalPemesananPanelDua = new JTextField();
		txtTanggalPemesananPanelDua.setBounds(173, 71, 114, 20);
		panelRincianDataPemesan.add(txtTanggalPemesananPanelDua);
		txtTanggalPemesananPanelDua.setColumns(10);
		
		btnRekamPemesanan = new JButton("Rekam Pemesanan");
		btnRekamPemesanan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rekamDataPemesanan();
			}
		});
		btnRekamPemesanan.setBounds(454, 388, 152, 26);
		panelDua.add(btnRekamPemesanan);
		
		panelTiga = new JPanel();
		tabbedPane.addTab("Data Pemesan", null, panelTiga, null);
		panelTiga.setLayout(null);
		
		JLabel lblDaftarKonserPanelTiga = new JLabel("Daftar Konser");
		lblDaftarKonserPanelTiga.setFont(new Font("Dialog", Font.BOLD, 14));
		lblDaftarKonserPanelTiga.setBounds(12, 12, 97, 19);
		panelTiga.add(lblDaftarKonserPanelTiga);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 38, 418, 140);
		panelTiga.add(scrollPane_1);
		
		listDaftarKonserPanelTiga = new JList();
		listDaftarKonserPanelTiga.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				ubahDaftarPemesanPadaKonserPanelTiga();
			}
		});
		scrollPane_1.setViewportView(listDaftarKonserPanelTiga);
		listDaftarKonserPanelTiga.setBorder(new LineBorder(new Color(0, 0, 0)));
		listDaftarKonserPanelTiga.setFont(new Font("Dialog", Font.BOLD, 14));
		
		lblNewLabel = new JLabel("Daftar Pemesan");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel.setBounds(12, 190, 110, 19);
		panelTiga.add(lblNewLabel);
		
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(12, 221, 415, 137);
		panelTiga.add(scrollPane_2);
		
		listDaftarPemesanPanelTiga = new JList();
		listDaftarPemesanPanelTiga.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				ubahRincianPemesanPanelTiga();
			}
		});
		scrollPane_2.setViewportView(listDaftarPemesanPanelTiga);
		listDaftarPemesanPanelTiga.setFont(new Font("Dialog", Font.BOLD, 14));
		listDaftarPemesanPanelTiga.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		lblJenisTiketPanelTiga = new JLabel("Jenis Tiket : ");
		lblJenisTiketPanelTiga.setHorizontalAlignment(SwingConstants.LEFT);
		lblJenisTiketPanelTiga.setFont(new Font("Dialog", Font.BOLD, 14));
		lblJenisTiketPanelTiga.setBounds(12, 370, 152, 19);
		panelTiga.add(lblJenisTiketPanelTiga);
		
		lblTanggalPemesananPanelTiga = new JLabel("Tanggal Pemesanan : ");
		lblTanggalPemesananPanelTiga.setHorizontalAlignment(SwingConstants.LEFT);
		lblTanggalPemesananPanelTiga.setFont(new Font("Dialog", Font.BOLD, 14));
		lblTanggalPemesananPanelTiga.setBounds(12, 402, 152, 19);
		panelTiga.add(lblTanggalPemesananPanelTiga);
		
		cmbJenisTiketPanelTiga = new JComboBox();
		cmbJenisTiketPanelTiga.setModel(new DefaultComboBoxModel(new String[] {"Festival", "Tribun", "VIP"}));
		cmbJenisTiketPanelTiga.setFont(new Font("Dialog", Font.BOLD, 14));
		cmbJenisTiketPanelTiga.setBounds(170, 370, 114, 25);
		panelTiga.add(cmbJenisTiketPanelTiga);
		
		txtTanggalPemesananPanelTiga = new JTextField();
		txtTanggalPemesananPanelTiga.setBounds(170, 402, 114, 20);
		panelTiga.add(txtTanggalPemesananPanelTiga);
		txtTanggalPemesananPanelTiga.setColumns(10);
		btnTambahData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				apakahTambahData = true;
				masukModeEditing();
				btnRekamDataKonser.setEnabled(true);
				btnRekamPerubahan.setEnabled(false);
			}
		});
	}
	
	void rekamDataKonser() {
		// 1. Membaca input User dari GUI
		String namaKonser = "";
		namaKonser = txtNamaKonser.getText();
		
		char jenisTiket = 'F';
		if (cmbJenisTiket.getSelectedIndex()==1) {
			jenisTiket = 'T';
		}
		else if (cmbJenisTiket.getSelectedIndex()==2) {
			jenisTiket = 'V';
		}
		
		int jumlahTiket = 0;
		int sisaTiket = 0;
		try {
			jumlahTiket = Integer.parseInt(txtJumlahTiket.getText());
			txtSisaTiket.setText("" + jumlahTiket);
			sisaTiket = jumlahTiket;
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, 
					"Isian Jumlah Tiket harus berupa bilangan bulat");
			return;
		}
		
		// 2. Memulai transaction baru
		PersistentTransaction t = null; 
		try {
			t = TugasBesar02PersistentManager
					.instance().getSession().beginTransaction();
		} catch (PersistentException e) {
			JOptionPane.showMessageDialog(null, 
					"Gagal memulai transaction\n"
					+ "Pesan Error : " + e.getMessage());
			return;
		}
		
		/* 3. Membungkus semua proses perubahan dalam try-catch agar bisa dibatalkan
		 *    (roll-back) seandainya terjadi error
		 */	
		try {
			// 3a. Membuat objek konser baru (memori + database)
			Konser konserBaru = KonserDAO.createKonser();
			// 3b. Mengubah atribut objek konser baru
			konserBaru.setNamaKonser(namaKonser);
			konserBaru.setJenisTiket(jenisTiket);
			konserBaru.setJumlahTiket(jumlahTiket);
			konserBaru.setSisaTiket(sisaTiket); 
			// 3c. Rekam perubahan ke dalam database
			KonserDAO.save(konserBaru);
			// 3d. Jika semua perubahan berhasil, maka lakukan commit
			t.commit();
			JOptionPane.showMessageDialog(null, 
					"Data konser baru berhasil ditambahkan");
		} catch (PersistentException e) {
			JOptionPane.showMessageDialog(null, 
					"Gagal merekam data konser\n"
					+ "Pesan Error : " + e.getMessage());
			try {
				t.rollback();
			} catch (PersistentException e1) {
				JOptionPane.showMessageDialog(null, 
						"Gagal melakukan rollback\n"
						+ "Pesan Error : " + e1.getMessage());
			}
			return;
		}
		bacaDataDariDatabase();
		tampilkanDataKonserPadaList();
		int indeksDipilih = dataKonser.length - 1;
		pilihSebuahKonserPadaList(indeksDipilih);
		masukModeBrowsing();
	}
	
	void rekamPerubahan() {
	// 1. Membaca input User dari GUI
		String namaKonser = "";
		namaKonser = txtNamaKonser.getText();
				
		char jenisTiket = 'F';
		if (cmbJenisTiket.getSelectedIndex()==1) {
			jenisTiket = 'T';
		}
		else if (cmbJenisTiket.getSelectedIndex()==2) {
			jenisTiket = 'V';
		}
		int jumlahTiket = 0;
		int sisaTiket = 0;
		try {
			jumlahTiket = Integer.parseInt(txtJumlahTiket.getText());
					txtSisaTiket.setText("" + jumlahTiket);
					sisaTiket = jumlahTiket;
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, 
							"Isian Jumlah Tiket harus berupa bilangan bulat");
					return;
				}
				
				// 2. Memulai transaction baru
				PersistentTransaction t = null; 
				try {
					t = TugasBesar02PersistentManager
							.instance().getSession().beginTransaction();
				} catch (PersistentException e) {
					JOptionPane.showMessageDialog(null, 
							"Gagal memulai transaction\n"
							+ "Pesan Error : " + e.getMessage());
					return;
				}
				
				/* 3. Membungkus semua proses perubahan dalam try-catch agar bisa dibatalkan
				 *    (roll-back) seandainya terjadi error
				 */	
				try {
					int index = listDaftarKonser.getSelectedIndex();
					Konser konserBaru = (Konser) listDaftarKonser
								.getModel().getElementAt(index);
					// 3b. Mengubah atribut objek konser baru
					konserBaru.setNamaKonser(namaKonser);
					konserBaru.setJenisTiket(jenisTiket);
					konserBaru.setJumlahTiket(jumlahTiket);
					konserBaru.setSisaTiket(sisaTiket); 
					// 3c. Rekam perubahan ke dalam database
					KonserDAO.save(konserBaru);
					// 3d. Jika semua perubahan berhasil, maka lakukan commit
					t.commit();
					JOptionPane.showMessageDialog(null, 
							"Data konser berhasil diubah");
				} catch (PersistentException e) {
					JOptionPane.showMessageDialog(null, 
							"Gagal merubah data konser\n"
							+ "Pesan Error : " + e.getMessage());
					try {
						t.rollback();
					} catch (PersistentException e1) {
						JOptionPane.showMessageDialog(null, 
								"Gagal melakukan rollback\n"
								+ "Pesan Error : " + e1.getMessage());
					}
					return;
				}
				//bacaDataDariDatabase();
				int indeksDipilih = listDaftarKonser
						.getSelectedIndex();
				tampilkanDataKonserPadaList();
				//int indeksDipilih = dataKonser.length - 1;
				pilihSebuahKonserPadaList(indeksDipilih);
				masukModeBrowsing();
	}
	
	void tambahDataKonser() {
		masukModeEditing();
		btnRekamDataKonser.setText("Rekam Data Konser");
		txtNamaKonser.setText("");
		cmbJenisTiket.setSelectedIndex(0);
		txtJumlahTiket.setText("");
		txtSisaTiket.setText("");
	}
	
	void ubahData() {
		aktifkanPanelRincian();
	}
	
	void masukModeEditing() {
		aktifkanPanelRincian();
		panelRincianDataKonser.setVisible(true);
		listDaftarKonser.setEnabled(false);
		btnTambahData.setEnabled(false);
		btnUbahData.setEnabled(false);
		btnHapus.setEnabled(false);
//		btnRekamDataKonser.setVisible(true);
		btnRekamDataKonser.setEnabled(true);
		btnRekamPerubahan.setVisible(true);
//		btnBatal.setVisible(true);
		btnBatal.setEnabled(true);
		if (apakahTambahData==true) {
			txtNamaKonser.setText("");
			cmbJenisTiket.setSelectedIndex(0);
			txtJumlahTiket.setText("");
			txtSisaTiket.setText("");
		} 
	}
	
	void masukModeBrowsing() {
		nonAktifkanPanelRincian();
		listDaftarKonser.setEnabled(true);
//		btnRekamDataKonser.setVisible(false);
		btnRekamDataKonser.setEnabled(false);
		btnRekamPerubahan.setEnabled(false);
//		btnBatal.setVisible(false);
		btnBatal.setEnabled(false);
		btnTambahData.setEnabled(true);
		btnUbahData.setEnabled(true);
		btnHapus.setEnabled(true);
	}
	
	void bacaDataDariDatabase() {
		try {
			dataKonser = KonserDAO.listKonserByQuery(null, null);
		} catch (PersistentException e) {
			JOptionPane.showMessageDialog(null, 
					"Gagal membaca data konser\n"
					+ "Pesan Error : " + e.getMessage());
		}
	}
	
	void tampilkanDataKonserPadaList() {
		listDaftarKonser.setListData(dataKonser);
		listDaftarKonserPanelTiga.setListData(dataKonser);
	}
	
	void ubahTampilanPadaPanelRincian() {
		if (listDaftarKonser.getModel().getSize()==0) {
			/* Jika list data konser kosong, maka kosongkan
			 * juga panel rincian data konser
			 */
			txtNamaKonser.setText("");
			txtJumlahTiket.setText("");
			txtSisaTiket.setText("");
			cmbJenisTiket.setSelectedIndex(0);
			return;
		}
		int index = listDaftarKonser.getSelectedIndex();
		if (index==-1) {
			return;
		}
//		if (index==-1) {
//			index = dataKonser.length - 1;
//		}
		Konser konserDipilihPadaList = (Konser) listDaftarKonser
				.getModel().getElementAt(index);
		txtNamaKonser.setText(konserDipilihPadaList.getNamaKonser());
		cmbJenisTiket.setSelectedIndex(0);
		if (konserDipilihPadaList.getJenisTiket()=='T') {
			cmbJenisTiket.setSelectedIndex(1);
		}
		else if (konserDipilihPadaList.getJenisTiket()=='V') {
			cmbJenisTiket.setSelectedIndex(2);
		}
		txtJumlahTiket.setText("" + konserDipilihPadaList.getJumlahTiket());
		txtSisaTiket.setText("" + konserDipilihPadaList.getSisaTiket());	
	}
	
	void batalkanProsesManipulasiData() {
		/*1. Aktifkan/NonAktifkan Tombol GUI */
		masukModeBrowsing();
		
		/*2. Update Tampilan Isian Data Sesuai Pilihan pada list */
		ubahTampilanPadaPanelRincian();
	}
	
	void hapusData() {
		int pilihanKonfirmasi = JOptionPane.showConfirmDialog(null, 
				"Apakah anda yakin untuk menghapus data konser " 
				+ listDaftarKonser.getSelectedValue(),
				"Konfirmasi Hapus Data",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (pilihanKonfirmasi == JOptionPane.OK_OPTION) {
		// 1. Menentukan objek konser yang sedang dipilih pada list
		int index = listDaftarKonser.getSelectedIndex();
		Konser konserDipilihPadaList = (Konser) listDaftarKonser
					.getModel().getElementAt(index);
			// 2. Hapus data dari database
			PersistentTransaction t = null;
			int indeksDataDipilih = listDaftarKonser.getSelectedIndex();
			int jumlahKonser = dataKonser.length;
			if (indeksDataDipilih == jumlahKonser-1 && jumlahKonser >= 2) {
				/* Yang dipilih adalah yang terakhir dan masih
				 * ada sisa konser belum dihapus di dalam list
				 */
				indeksDataDipilih -= 1;
			} else if (indeksDataDipilih == 0 && jumlahKonser == 1) {
				// Yang dipilih adalah satu-satunya konser dalam list
				indeksDataDipilih = -1;
			} 
			try {
				t = TugasBesar02PersistentManager
						.instance().getSession().beginTransaction();
				KonserDAO.deleteAndDissociate(konserDipilihPadaList);
				t.commit();
			} catch (PersistentException e) {
				try {
					JOptionPane.showMessageDialog(null, 
							"Gagal hapus data\n"
							+ "Pesan Error : " + e.getMessage());
					t.rollback();
				} catch (PersistentException e1) {
					JOptionPane.showMessageDialog(null, 
							"Gagal melakukan rollback\n"
							+ "Pesan Error : " + e1.getMessage());
				}
				return;
			}
			bacaDataDariDatabase();
			tampilkanDataKonserPadaList();	
			pilihSebuahKonserPadaList(indeksDataDipilih);
		}
	}
	
	void pilihSebuahKonserPadaList(int indeksDipilih) {
		if (indeksDipilih==-1) {
			return;
		}
		listDaftarKonser.setSelectedIndex(indeksDipilih);
	}
	
	void aktifkanPanelRincian() {
		for (Component guiObject:panelRincianDataKonser
				.getComponents()) {
			guiObject.setEnabled(true);
		}
	}
	
	void nonAktifkanPanelRincian() {
		for (Component guiObject:panelRincianDataKonser
				.getComponents()) {
			guiObject.setEnabled(false);
		}
	}
	
	void cariKonser() {
		// 1. Membuat string query
		String kondisi = String.format("jenis_tiket = " + "'%s' and sisa_tiket > 0" , 
				rdbtnFestival.isSelected() ? "F" : rdbtnTribun.isSelected() ? "T" : "V"  );
		//2. Mengeksekusi hasil query dengan kondisi dan
		//Menyimpan hasil query ke dalam array
		dataKonserSesuai = new Konser[0];
		try {
			dataKonserSesuai = KonserDAO.listKonserByQuery(kondisi,
					"sisa_tiket DESC");
		} catch (PersistentException e) {
			JOptionPane.showMessageDialog(null, "Gagal"
					+ "membaca data konser\n"
					+ "Pesan error:" + e.getMessage());
			
			return;
		}
		
		/* 3. Menampilkan data konser yang sesuai
		 * pada list daftar konser sesuai, jika ada minimal
		 * 1 konser sebagai hasil query
		 */
		
		if(dataKonserSesuai.length>0) {
			listDaftarKonserSesuai.setListData(dataKonserSesuai);
		} else {
			JOptionPane.showMessageDialog(null, 
					"Tidak tersedia konser yang masih memiliki"
					+ "tiket kosong");
			return;
		}
	}
	
	void ubahRincianDataKonserDipilih() {
		/* 1. Memeriksa apakah event list selection change dipicu
		 * oleh perubahan pilihan user pada list atau oleh proses
		 * menampilkan data pad list
		 */
		
		int indeksDipilih = listDaftarKonserSesuai.getSelectedIndex();
		if (indeksDipilih == -1) {
			txtNamaKonserPanelDua.setText("");
			txtJumlahTiketPanelDua.setText("");
			txtSisaTiketPanelDua.setText("");
			return;
		}
		
		/* 2. Menentukan objek kamar yang dipilih
		 * user pada list
		 */
		Konser konserDipilih = (Konser) listDaftarKonserSesuai.getModel()
				.getElementAt(indeksDipilih);
	
	/* 3. Mengisi rincian data konser sesuai yang dipilih user
	 * 
	 */
	txtNamaKonserPanelDua.setText(""+konserDipilih.getNamaKonser());
	txtJumlahTiketPanelDua.setText(""+konserDipilih.getJumlahTiket());
	txtSisaTiketPanelDua.setText(""+konserDipilih.getSisaTiket());
	}
	void rekamDataPemesanan() {
		//1. Memeriksa apakah konser sudah dipilih
		if (listDaftarKonserSesuai.getSelectedIndex()==-1) {
			JOptionPane.showMessageDialog(null, 
					"Konser untuk pemesan harus dipilih terlebih dahulu. \n"
					+ "Silahkan Perbaiki.");
			return;
		}
		
		/* 2. Menentukan objek kamar yang dipilih
		 * user pada list
		 */
		int indeksDipilih = listDaftarKonserSesuai.getSelectedIndex();
		Konser konserDipilih = (Konser) listDaftarKonserSesuai.getModel()
				.getElementAt(indeksDipilih);
	
		/*3. Memeriksa apakah nama pemesan sudah diisi 
		 */
		if(txtNamaPemesan.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Nama pemesan harus diisi terlebih dahulu.\n"
				+ "Silahkan Perbaiki");
			return;
		}
		String namaPemesan = txtNamaPemesan.getText();
		
		/* 4. Memeriksa apakah tanggal pemesanan adalah tanggal yang valid
		 */
		Date tanggalPemesanan = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//		Date tanggalPemesanan = null;
//		try {
//			tanggalPemesanan = sdf.parse(txtTanggalPemesananPanelDua
//					.getText());
//		} catch (ParseException e2) {
//			JOptionPane.showMessageDialog(null, 
//					"Tanggal pemesanan harus ditulis dengan format"
//					+ "DD/MM/YYYY.\n"
//					+ "Silahkan perbaiki.");
//			return;
//		}
		
		// 5. Merekam data pendaftaran ke dalam database
		// 5a. Membuat transaction
		PersistentTransaction t = null;
		try {
			t = TugasBesar02PersistentManager.instance().getSession().beginTransaction();
		} catch (PersistentException e1) {
			JOptionPane.showMessageDialog(null, 
					"Gagal membuat transaction.\n"
					+ "Pesan Error : " + e1.getMessage());
		}
		
		try {
			// 5b. Melakukan langkah perubahan data
			// 5b1. Membuat objek pemesan baru
			Pemesan pemesanBaru = PemesanDAO.createPemesan();
			// 5b2. Mengubah berbagai atribut objek pemesan
			pemesanBaru.setNamaPemesan(namaPemesan);
			pemesanBaru.setTanggalPemesanan(tanggalPemesanan);
			// 5b3. Mengubah berbagai atribut objek konser
			konserDipilih.pemesan.add(pemesanBaru);
			konserDipilih.setSisaTiket(konserDipilih
					.getSisaTiket() - 1);
			// 5b4. Merekam perubahan ke dalam database
			PemesanDAO.save(pemesanBaru);
			KonserDAO.save(konserDipilih);
			t.commit();
		} catch (PersistentException e) {
			JOptionPane.showConfirmDialog(null, 
					"Gagal merekam data pemesan\n"
					+ "Pesan Error : " + e.getMessage());
			try {
				t.rollback();
			} catch (PersistentException e1) {
				JOptionPane.showMessageDialog(null, 
						"Gagal melakukan rollback.\n"
						+ "Pesan Error : " + e1.getMessage());
			}
			return;
		}
		JOptionPane.showMessageDialog(null, 
				"Data pemesan berhasil ditambahkan");
		txtNamaPemesan.setText("");
		txtJumlahTiketPanelDua.setText("");
		txtSisaTiketPanelDua.setText("");
		listDaftarKonserSesuai.setListData(new Konser[0]);
		rdbtnFestival.setSelected(true);
	}
	
	void ubahDaftarPemesanPadaKonserPanelTiga() {
		/* 1. Memeriksa apakah event selection-change dipicu oleh
		 * berubahnya pilihan user pada list atau dipicu oleh awal 
		 * ditampilkannya daftar konser pada list
		 */
		int indeksDipilih = listDaftarKonserPanelTiga.getSelectedIndex();
		if (indeksDipilih==-1) {
			// Dipicu oleh awal ditampilkannya daftar
			return;
		}
		// 2. Mendapatkan objek konser yang dipilih pada list
		Konser konserDipilih = (Konser) listDaftarKonserPanelTiga.getModel()
				.getElementAt(indeksDipilih);
		// 3. Membuat daftar pemesan
		/* 3a. Membuat array objek pemesan dengan jumlah elemen sebanyak 
		 * jumlah objek pemesan dalam koleksi pemesan pada objek konser dipilih
		 */
		Pemesan[] dataPemesan = new Pemesan[konserDipilih.pemesan.size()];
		/* 3b. Konversi koleksi pemesan menjadi array */
		dataPemesan = (Pemesan[]) konserDipilih.pemesan.getCollection()
				.toArray(dataPemesan);
		// 4. Menampilkan daftar pemesan pada list
		listDaftarPemesanPanelTiga.setListData(dataPemesan);
	}
	
	void ubahRincianPemesanPanelTiga() {
		/* 1. Memeriksa apakah perubahan pilihan list dipicu oleh 
		 * berubahnya pilihan user atau dipicu oleh baru
		 * ditampilkannya daftar pemesan
		 */
		int indeksDipilih = listDaftarPemesanPanelTiga.getSelectedIndex();
		if (indeksDipilih==-1) {
			// Dipicu oleh baru ditampilkannya daftar
			if (listDaftarPemesanPanelTiga.getModel().getSize()>0) {
				// Jika list tidak kosong, pilih pemesan pada daftar paling atas
				listDaftarPemesanPanelTiga.setSelectedIndex(0);
			}
			return;
		}
		// 2. Ubah rincian
		cmbJenisTiketPanelTiga.setSelectedIndex(0);
		Pemesan pemesanDipilih = (Pemesan) listDaftarPemesanPanelTiga
				.getModel().getElementAt(indeksDipilih);
		if (pemesanDipilih.getJenisTiket()=='T') {
			cmbJenisTiketPanelTiga.setSelectedIndex(1);
		} else if (pemesanDipilih.getJenisTiket()=='V') {
			cmbJenisTiketPanelTiga.setSelectedIndex(2);
		}
		String tanggal = "";
		LocalDate tanggalPemesanan = Instant.ofEpochMilli(pemesanDipilih
				.getTanggalPemesanan().getTime())
				.atZone(ZoneId.systemDefault())
				.toLocalDate();
		tanggal += tanggalPemesanan.getDayOfMonth() + "/";
		tanggal += tanggalPemesanan.getMonthValue() + "/";
		tanggal += tanggalPemesanan.getYear();
		txtTanggalPemesananPanelTiga.setText(tanggal);
	}
}