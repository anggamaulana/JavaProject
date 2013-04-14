/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fuzzysugeno;

/**
 *
 * @author angga
 */
public class rumus {

    private int permintaan;
    private int persediaan;
    private double produksi;
    
    private double[] derajatPermintaan;
    private double[] derajatPersediaan;
    
    //just like matlab format..
    private int[]bataspermintaanturun = new int[]{2000,2500,5500};
    private int[]bataspermintaannaik = new int[]{2500,5500,6000};
    
    //fill this range just like what you do in matlab...
    private int[]bataspersediaansedikit = new int[]{200,250,750};
    private int[]bataspesediaanbanyak = new int[]{250,750,800};
    
    
    
    public rumus(){
        
        
    }
    
    public void setPermintaan(int a){
        this.permintaan=a;
    }
    
     public void setPesediaan(int b){
        this.persediaan=b;
    }
    
    
    public int getProduksi(){
        return (int)this.produksi;
    }
    
    public void hitung(){
        fuzzykikasi(this.permintaan, this.persediaan);
        implikasi();
        
    }
    
    private void fuzzykikasi(int permintaan,int persediaan){
        this.derajatPermintaan = new double[2];
        this.derajatPersediaan = new double[2];
        
        if(permintaan>=this.bataspermintaanturun[0] && permintaan <=this.bataspermintaanturun[1]){
            this.derajatPermintaan[0]=1;
        }else  if(permintaan>=this.bataspermintaannaik[1] && permintaan<=this.bataspermintaannaik[2]){
            this.derajatPermintaan[1]=1;
        }else  if(permintaan>=2500 && permintaan<=5500){
            //count below
            
            //turun
            this.derajatPermintaan[0]=(double)(this.bataspermintaanturun[2]-permintaan)/(this.bataspermintaanturun[2]-this.bataspermintaanturun[1]);
            
            //naik
            this.derajatPermintaan[1]=(double)(permintaan-this.bataspermintaannaik[0])/(this.bataspermintaannaik[1]-this.bataspermintaannaik[0]);
           
           
        
        }else{
            throw new IllegalArgumentException("permintaan tidak dalam range 2000-6000");
        }
        
        if(persediaan>=this.bataspersediaansedikit[0] && persediaan <=this.bataspersediaansedikit[1]){
            this.derajatPersediaan[0]=1;
        }else if(persediaan>=this.bataspesediaanbanyak[1] && persediaan<=this.bataspesediaanbanyak[2]){
            this.derajatPersediaan[1]=1;
        }else if(persediaan>=250 && persediaan<=750){
            //count the result below
             //sedikit
            
            this.derajatPersediaan[0]=(double)(this.bataspersediaansedikit[2]-persediaan)/(this.bataspersediaansedikit[2]-this.bataspersediaansedikit[1]);
            
            //banyak
            this.derajatPersediaan[1]=(double)(persediaan-this.bataspesediaanbanyak[0])/(this.bataspesediaanbanyak[1]-this.bataspesediaanbanyak[0]);
            
            
        }else{
            throw new IllegalArgumentException("persediaan tidak dalam range 200-800");
        }
        
        
        
        
        
    }
    
    
    private void implikasi(){
        double[] derajatkeanggotaan = new double[4];
        double Z[] = new double[4];
        //rule 1
        derajatkeanggotaan[0] = min(this.derajatPermintaan[0],this.derajatPersediaan[1]);
        derajatkeanggotaan[1] = min(this.derajatPermintaan[0],this.derajatPersediaan[0]);
        derajatkeanggotaan[2] = min(this.derajatPermintaan[1],this.derajatPersediaan[1]);
        derajatkeanggotaan[3] = min(this.derajatPermintaan[1],this.derajatPersediaan[0]);
        
        Z[0]=this.permintaan-this.persediaan;
        Z[1]=this.permintaan;
        Z[2]=this.permintaan;
        Z[3]=(1.5*this.permintaan)-this.persediaan;
        
        
        double a=0,b=0;
        
        for(int i=0;i<4;i++){
            a+=(derajatkeanggotaan[i]*Z[i]);
            b+=derajatkeanggotaan[i];
            
          // System.out.println(derajatkeanggotaan[i]+" "+Z[i]+" "+a);
        }
        //System.out.println(a+" "+b);
        this.produksi = a/b;
        
        
    }
    
    
    private double min(double a,double b){
 
     if(a<b)
         return a;
     
     if(b<a)
         return b;
     
     return a;
     
     
     
    }
}

