/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kartupasien;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.awt.image.RenderedImage;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Angga
 */
public class database {
    
    public Connection con;
    private Statement st;
    private PreparedStatement ps;
    private ResultSet rs;
    private javax.swing.JFrame parent;
    
    public database(javax.swing.JFrame parent){
        
        this.parent = parent;
        connection();
        
    }
    
    private void connection(){
        try{
             Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            String databasefile = System.getProperty("user.dir")+"\\kartupasien.mdb";
             this.con = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ="+databasefile);
             
        }catch(Exception e){
            e.printStackTrace();
           
            JOptionPane.showMessageDialog(null,"Database tidak ditemukan");
            
        }
    }
    
    
    public TableModel ambilDataPasien(String param){
        TableModel hasil = null;
        try {
            String kt = param.trim();
            String []kata = kt.split(" ");
            
            
            String sql = "select p.id_pasien as ID,p.nama as Nama,p.alamat as Alamat,p.umur as Umur,"
                    + "p.kelamin as JenisKelamin from pasien p";
          
            if(param.compareTo("")!=0){
                sql+=" where ";
                for(int i=0;i<kata.length;i++){
                    sql+=" p.nama like '%"+esc(kata[i])+"%'";
                    if(i<kata.length-1)
                        sql+=" or ";
                }
            }
           
           
            st = this.con.createStatement();            
            rs = st.executeQuery(sql);
            
            hasil = DbUtils.resultSetToTableModel(rs);
            
            
            
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Maaf ada kesalahan dengan database");
        }finally{
            try {
                rs.close();
                st.close();
                
            } catch (SQLException ex) {
                Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
            }
            return hasil;
        }
        
    }
    
     public TableModel ambilDataPasienall(){
        TableModel hasil = null;
        Statement st1=null;
        ResultSet rs1=null;
        try {
          
            
            
            String sql = "select p.id_pasien as ID,p.nama as Nama,p.alamat as Alamat,p.umur as Umur,p.kelamin as JenisKelamin from pasien p";
          
                      
            
           st1 = this.con.createStatement();                  
           rs1 = st1.executeQuery(sql);
           
           
          
           hasil = DbUtils.resultSetToTableModel(rs1);    
           
            
            
            
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Maaf ada kesalahan dengan database");
        }finally{
            try {
                rs1.close();
                st1.close();
                
            } catch (SQLException ex) {
                Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
            }
            return hasil;
        }
        
    }
     
     
     public int jumlahBarisTabelPasien(){
         int hasil=0;
        try {
            
            
            String sql = "select count(id_pasien) as jumlah from pasien";
            
             
            
            st = this.con.createStatement();            
            rs = st.executeQuery(sql);
            
            if(rs.next()){
               
                hasil = rs.getInt("jumlah");
             
            }
            
           rs.close();
            st.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Maaf ada kesalahan dengan database"+ex);
        }finally{
            return hasil;
        }
         
     }
    
    
     public String[] ambilDataPasien(int idpasien){
        String[] hasil = new String[4];
        try {
            
            
            String sql = "select * from pasien where id_pasien="+idpasien;
            
             
            
            st = this.con.createStatement();            
            rs = st.executeQuery(sql);
            
            if(rs.next()){
               
                hasil[0] = unesc(rs.getString(2));
                hasil[1] = unesc(rs.getString(3));
                hasil[2] = unesc(rs.getString(4));
                hasil[3] = unesc(rs.getString(5));
            }
            
           rs.close();
            st.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Maaf ada kesalahan dengan database"+ex);
        }finally{
            return hasil;
        }
        
    }
     
     
      public String[] ambilDataRiwayatPasien(int idriwayat){
        String[] hasil = new String[6];
        try {
            
            
            String sql = "select * from riwayat where ID="+idriwayat;
            
             
            
            st = this.con.createStatement();            
            rs = st.executeQuery(sql);
           
            if(rs.next()){
                hasil[0] = unesc(rs.getString(3));
                hasil[1] = unesc(rs.getString(4));
                hasil[2] = unesc(rs.getString(5));
                hasil[3] = unesc(rs.getString(6));
                hasil[4] = unesc(rs.getString(7));
                hasil[5] = String.valueOf(rs.getInt(10));
               
            }
            
           rs.close();
            st.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Maaf ada kesalahan dengan database"+ex);
        }finally{
            return hasil;
        }
        
    }
      
       public List<String[]> ambilDataRiwayatPasienID(int idpasien){
        List<String[]>  hasil = null;
        try {
            
            
            String sql = "select * from riwayat where id_pasien="+idpasien;
            
             
            
            st = this.con.createStatement();            
            rs = st.executeQuery(sql);
           List<String[]> a = new ArrayList();
          
            while(rs.next()){
                a.add(new String[]{unesc(rs.getString(3)),unesc(rs.getString(4)),
                unesc(rs.getString(5)),unesc(rs.getString(6)),unesc(rs.getString(7))});         
                
               
            }
            hasil = a;
           rs.close();
            st.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Maaf ada kesalahan dengan database"+ex);
        }finally{
            return hasil;
        }
        
    }
      
       public int notterakhir(){
       int hasil=0;
        try {
            
            
            String sql = "select max(r.ID) as id from riwayat r";
            
             
            
            st = this.con.createStatement();            
            rs = st.executeQuery(sql);
            
            if(rs.next()){
               
                hasil = rs.getInt("id");
             
            }
            
           rs.close();
            st.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Maaf ada kesalahan dengan database"+ex);
        }finally{
            return hasil;
        }
        
    }
       
        public int notterakhirpasien(){
       int hasil=0;
        try {
            
            
            String sql = "select max(p.id_pasien) as id from pasien p";
            
             
            
            st = this.con.createStatement();            
            rs = st.executeQuery(sql);
            
            if(rs.next()){
               
                hasil = rs.getInt("id");
             
            }
            
           rs.close();
            st.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Maaf ada kesalahan dengan database"+ex);
        }finally{
            return hasil;
        }
        
    }
        
    public String[] ambilDataGigi(int id){
        
        String hasil[] = new String[2];;
        
        try {
            String sql = "select tipegigi,gigi from riwayat where ID="+id;
            this.st = this.con.createStatement();
            this.rs = this.st.executeQuery(sql);
            if(this.rs.next()){
                hasil[0] = this.rs.getString(1);
                hasil[1] = this.rs.getString(2);
            }
            
            this.rs.close();
            this.st.close();
            
            return hasil;
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return hasil;
        
        
        
        
    }
    
    
    public TableModel DataRiwayatPasien(int idPasien){
        TableModel hasil = null;
        try {
            
            
            String sql = "select ID,tanggal,diagnose,obat,tindakan,keterangan,tipegigi,harga from riwayat where id_pasien="+idPasien+" order by tanggal asc";
          
             
            
            st = this.con.createStatement();             
            rs = st.executeQuery(sql);
            
            hasil = DbUtils.resultSetToTableModel(rs);
           rs.close();
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return hasil;
        }
        
    }
    
    public boolean tambahdatapasien(String nama,String alamat,int umur,String kelamin){
       boolean hasil = false;
        try {
            
            
            String sql = "insert into pasien(nama,alamat,umur,kelamin) values('"
                    + esc(nama)+"','"
                    + esc(alamat)+"',"
                    + umur+",'"
                    + esc(kelamin)+"')";
          
             
            
            st = this.con.createStatement();     
           
            st.executeUpdate(sql);       
            
            
           
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }finally{
            return hasil;
        }
        
        
        
    }

    public boolean tambahRiwayatpasien(List<Object[]> rowTambahan) {
      boolean hasil = false;
        try {
               
            for(int i=0;i<rowTambahan.size();i++){
                
             
             String namagambar = String.valueOf(rowTambahan.get(i)[1])+"_"+String.valueOf(notterakhir()+1)+".jpeg";   
             Image img = (Image)rowTambahan.get(0)[10];
             BufferedImage bf = (BufferedImage)img;
             File f = new File("img/"+namagambar);             
             RenderedImage rd = bf;               
             ImageIO.write(rd, "jpeg", f);
                
                st = this.con.createStatement();
                String sql = "insert into riwayat(id_pasien,diagnose,obat,tindakan,keterangan,tipegigi,gigi,harga,gambargigi) "
                        + "values("
                        + rowTambahan.get(i)[1]+","                        
                        + "'"+esc((String)rowTambahan.get(i)[3])+"',"
                        + "'"+esc((String)rowTambahan.get(i)[4])+"',"
                        + "'"+esc((String)rowTambahan.get(i)[5])+"',"
                         + "'"+esc((String)rowTambahan.get(i)[6])+"',"
                         + "'"+rowTambahan.get(i)[7]+"',"
                        + "'"+esc((String)rowTambahan.get(i)[8])+"',"
                        + ""+rowTambahan.get(i)[9]+","
                        + "'"+namagambar+"')";       
               
                     
                st.executeUpdate(sql);       

                st.close();
            }
            
             
             
            
            
        } catch (SQLException ex) {
            Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (ImagingOpException ex) {
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }finally{
            return hasil;
        }
    }
    
    public String esc(String word){
        return word.replaceAll("'","''");
    }
    
    
    public String unesc(String word){
        return word.replaceAll("''","'");
    }

    public TableModel ambilRentangWaktu(java.util.Date awal, java.util.Date akhir) {
      TableModel tm = null;
      SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
        String  a = sd.format(awal);
        String  b = sd.format(akhir);
        
        
        
        try{
            
            if(awal.equals(akhir)){
                    Calendar c = Calendar.getInstance();
                    c.setTime(awal);
                    c.add(Calendar.HOUR_OF_DAY, 24);
                    b = sd.format(c.getTime());
           
            }
            
            String sql = "select r.tanggal,p.nama,p.alamat,p.umur,r.diagnose,r.obat,r.harga from pasien p,riwayat r"
                    + " where (r.tanggal>=#"+a+"# and r.tanggal<=#"+b+"#) and r.id_pasien=p.id_pasien";
            
            
            
            
            this.st = this.con.createStatement();
            this.rs = this.st.executeQuery(sql);
            tm = DbUtils.resultSetToTableModel(rs);
            
            rs.close();
            st.close();
            return tm;
            
        }catch(Exception ex){
            
        }
        
        return tm;
        
    }
    
    
    public int[] jumlahTotalharga(java.util.Date awal,java.util.Date akhir){
        int[] jumlah=new int[3];
        SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
        String  a = sd.format(awal);
        String  b = sd.format(akhir);
        
        try{
            
            if(awal.equals(akhir)){
                    Calendar c = Calendar.getInstance();
                    c.setTime(awal);
                    c.add(Calendar.HOUR_OF_DAY, 24);
                    b = sd.format(c.getTime());
           
            }
        String sql = "select sum(r.harga),count(r.ID) as total from pasien p,riwayat r"
                    + " where (r.tanggal>=#"+a+"# and r.tanggal<=#"+b+"#) and r.id_pasien=p.id_pasien";
            
             
         
            
            this.st = this.con.createStatement();
            this.rs = this.st.executeQuery(sql);
            
            if(this.rs.next()){
             jumlah[0] =  this.rs.getInt(1);
             jumlah[1] =  this.rs.getInt(2);
            }
           
            
            rs.close();
            st.close();
            
             sql = "select distinct r.id_pasien as total from pasien p,riwayat r"
                    + " where (r.tanggal>=#"+a+"# and r.tanggal<=#"+b+"#) and r.id_pasien=p.id_pasien";
            
             
         
            
            this.st = this.con.createStatement();
            this.rs = this.st.executeQuery(sql);
           int count=0;
           while(this.rs.next()){
               count++;
           }
              
           jumlah[2] = count;
           
            
            rs.close();
            st.close();
            
            
            
            
            return jumlah;
            
            
            
        }catch(Exception ex){
            
        }
        return jumlah;
        
    }
    
    
    
}
