/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kartupasien;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;

/**
 *
 * @author Angga
 */
public class worker implements Runnable {

    database db;
    MainForm o;
    dataPasien p;
    String kata;
    int idpasien;
    String tanggal_awal;
    String tanggal_akhir;
    private int mode;
    private int work;
    public final static int TABELPASIEN = 0;
    public final static int TABELRIWAYATPASIEN = 1;
    public final static int TAMBAHPASIEN = 2;
    public final static int EXPORT = 3;
    public final static int REPORT = 4;
     public final static int REPORT_KEUANGAN = 5;
    private String path;
    private String[][] hasil;

    public worker(int mode, MainForm o, database db, String kata) {
        this.mode = mode;
        this.o = o;
        this.db = db;
        this.kata = kata;
    }

    public worker(int mode, dataPasien p, database db, int idpasien) {
        this.mode = mode;
        this.p = p;
        this.db = db;
        this.idpasien = idpasien;

    }

    public worker(int idpasien, int mode, String path, database db) {
        this.idpasien = idpasien;
        this.mode = mode;
        this.path = path;
        this.db = db;

    }
    public worker( int mode, String[][] hasil,String tanggal_awal,String tanggal_akhir,String path, database db) {
       
        this.mode = mode;
        this.tanggal_awal = tanggal_awal;
        this.tanggal_akhir = tanggal_akhir;
        this.db = db;
        this.path = path;
        this.hasil = hasil;

    }

    public void run() {
        try {
            if (this.mode == TABELPASIEN) {
                o.lbl_status.setText("Loading data..");
                o.tabelpasien.setModel(db.ambilDataPasien(this.kata));
                o.tabelpasien.getColumn("ID").setMaxWidth(35);
                o.tabelpasien.getColumn("Umur").setMaxWidth(35);
                o.jml_pasien.setText(String.valueOf(o.tabelpasien.getModel().getRowCount()));
                o.lbl_status.setText("");
            } else if (this.mode == TABELRIWAYATPASIEN) {
                p.lbl_status.setText("Loading data..");
                p.tabel_riwayat.setModel(db.DataRiwayatPasien(this.idpasien));
                p.tabel_riwayat.getColumn("ID").setMaxWidth(20);
                p.lbl_status.setText("");
            } else if (this.mode == EXPORT) {
                XWPFDocument document = new XWPFDocument();
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                String[] pasien = this.db.ambilDataPasien(this.idpasien);
                String[] kolom = {"Nama", "Alamat", "Umur", "Jenis Kelamin"};
                for (int i = 0; i < pasien.length; i++) {
                    run.setText(kolom[i] + "      :" + pasien[i]);
                    run.addBreak();
                }
                run.addBreak();
                run.setBold(true);
                run.setTextPosition(2);
                run.setText("Data Riwayat Kesehatan");
                run.setBold(false);
                run.addBreak();

                List<String[]> riwayat = this.db.ambilDataRiwayatPasienID(this.idpasien);

                if (riwayat != null) {
                    XWPFTable tabel = document.createTable(riwayat.size() + 1, 5);
                    tabel.getRow(0).getCell(0).setText("Tanggal");
                    tabel.getRow(0).getCell(1).setText("Diagnose");
                    tabel.getRow(0).getCell(2).setText("Obat");
                    tabel.getRow(0).getCell(3).setText("Tindakan");
                    tabel.getRow(0).getCell(4).setText("Keterangan");

                    for (int j = 0; j < riwayat.size(); j++) {
                        for (int i = 0; i < 5; i++) {
                            tabel.getRow(j + 1).getCell(i).setText(riwayat.get(j)[i]);
                        }
                    }

                }



                try {
                    this.path = this.path + ".docx";

                    FileOutputStream f = new FileOutputStream(this.path);
                    document.write(f);
                } catch (FileNotFoundException fe) {
                    fe.printStackTrace();
                }

            } else if (this.mode == REPORT) {
                try {
                    // TODO add your handling code here:
                  
                    this.p.lbl_status.setText("Loading Report");
                    HashMap m = new HashMap();
                    m.put("ID_PASIEN", this.idpasien);
                    m.put("", this.idpasien);

                    JasperReport jr = JasperCompileManager.compileReport(System.getProperty("user.dir")+"\\kartupasien2.jrxml");
                    JasperPrint jp = JasperFillManager.fillReport(jr, m, this.db.con);
                    JasperViewer jv = new JasperViewer(jp,false);
                    this.p.dispose();
                    jv.setVisible(true);
                    
                    

                }catch (JRException ex) {
                     
                   this.p.lbl_status.setText("Maaf Ada File Gambar Yang hilang");
                   ex.printStackTrace();
                }
            }else if (this.mode == REPORT_KEUANGAN) {
                XWPFDocument document = new XWPFDocument();
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                
                run.setText("Laporan Keuangan tanggal "+this.tanggal_awal+" Sampai Tanggal "+this.tanggal_akhir);
                run.addBreak();
                run.setBold(true);
                run.setTextPosition(2);
                run.setText("Data Riwayat Kesehatan");
                run.setBold(false);
                run.addBreak();

                

                if (this.hasil != null) {
                    if(this.hasil[0].length>0){
                        int baris = this.hasil.length;
                        int kolom = this.hasil[0].length;
                        
                        XWPFTable table = document.createTable(baris+1, kolom);
                        table.getRow(0).getCell(0).setText("Tanggal");
                        table.getRow(0).getCell(1).setText("Nama");
                        table.getRow(0).getCell(2).setText("Alamat");
                        table.getRow(0).getCell(3).setText("Umur");
                        table.getRow(0).getCell(4).setText("Obat");
                        table.getRow(0).getCell(5).setText("Diagnosa");
                        table.getRow(0).getCell(6).setText("Harga");
                        
                        int i=0,j=0;
                        for(i=0;i<baris;i++){
                            for(j=0;j<kolom;j++){
                                table.getRow(i+1).getCell(j).setText(this.hasil[i][j]);
                            }
                        }
                        
                        
                        run.addBreak();
                        run.setBold(true);
                        
                        run.setText("Total : "+this.hasil[this.hasil.length-1][1]);
                          run.addBreak();
                        run.setBold(true);
                        
                        run.setText("Total Pasien : "+this.hasil[this.hasil.length-1][0]);
                          run.addBreak();
                        run.setBold(true);
                        
                        run.setText("Total Baris : "+this.hasil[this.hasil.length-1][2]);
                    }
                    
                }



                try {
                    this.path = this.path;

                    FileOutputStream f = new FileOutputStream(this.path);
                    document.write(f);
                } catch (FileNotFoundException fe) {
                    fe.printStackTrace();
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            

        }
    }

    
}
