/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kartupasien;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 *
 * @author Angga
 */
public class DiagramGigi extends JPanel {

    private formRecordPasien parent;
    private statusGigi dialog;
    private int jumlahKolomGigi;
    private int tipeGigi;
    private int ruasGigi;
    private int width;
    private int XStart;
    private int YStart;
    private int widthMax=0;
    private int heightMax=0;
    private String[] indexGigiAnak = new String[]{"I", "II", "III", "IV", "V"};
    protected String[] statusGigiDewasa = new String[]{"","D", "M", "F", "I"};
    protected String[] statusGigiAnak = new String[]{"","d", "m", "f", "i"};
    public JPanel[] jp;
    private List informasiLetakGigi;
    protected List informasiGigi;
    protected List informasiGigiTransisi;
    public final static int DEWASA = 0;
    public final static int ANAK = 1;
    public final static int TRANSISI = 2;

    public DiagramGigi(formRecordPasien frp) {

        this.informasiLetakGigi = new ArrayList();
        this.width = 35;
        this.parent = frp;
    }

    public int getTipeGigi() {
        return this.tipeGigi;
    }

    public void addComp() {
        if (this.jp != null) {
            removeAllBoxGigi();
        }
        this.jp = new boxGigi[this.jumlahKolomGigi * 2];
        Point p;


        for (int i = 0; i < this.jumlahKolomGigi * 2; i++) {
            p = (Point) this.informasiLetakGigi.get(i);
            final int ind = i + 1;
            this.jp[i] = new boxGigi(i);

            this.jp[i].setSize(this.width, this.width);
            this.jp[i].setBounds(p.x, p.y, width, width);
            this.jp[i].setOpaque(false);

            this.jp[i].addMouseListener(new boxGigiAction(i, this));
            this.add(this.jp[i]);

        }
        
        this.heightMax = (width*2)+70;
        this.widthMax = (width*this.jumlahKolomGigi)+70;
    }

    public int getWidthMax(){
        return this.widthMax;
    }
    
    public int getheightMax(){
        return this.heightMax;
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {

            int indexGigi = 0;
            for (int j = 0; j < 2; j++) {
                for (int i = 0; i < this.jumlahKolomGigi; i++) {

                    int x = (i * width) + XStart;
                    int y = (j * width) + YStart;

                    Point p = new Point(x, y);
                    this.informasiLetakGigi.add(p);




                    g.setColor(Color.white);
                    g.fillRect(x, y, width, width);
                    g.setColor(Color.black);
                    g.drawRect(x, y, width, width);


                    if (this.tipeGigi == DiagramGigi.DEWASA) {
                        g.setColor(Color.blue);
                        String index = String.valueOf(8 - i);
                        if (i >= this.ruasGigi) {
                            index = String.valueOf(i - 7);
                        }
                        g.drawString(index, x + (width / 2), y + (width / 2));

                        g.setColor(Color.black);
                        if (j == 0) {
                            g.drawString((String) this.informasiGigi.get(indexGigi), x + (width / 2), y - (width / 2));
                        } else if (j == 1) {
                            g.drawString((String) this.informasiGigi.get(indexGigi), x + (width / 2), y + (width + (width / 2)));
                        }
                    } else if (this.tipeGigi == DiagramGigi.ANAK) {
                        g.setColor(Color.blue);
                        String index = "";
                        if (i < this.ruasGigi) {
                            index = this.indexGigiAnak[4 - i];
                        } else {
                            index = this.indexGigiAnak[i - 5];
                        }
                        g.drawString(index, x + (width / 2), y + (width / 2));

                        g.setColor(Color.black);
                        if (j == 0) {
                            g.drawString((String) this.informasiGigi.get(indexGigi), x + (width / 2), y - (width / 2));
                        } else if (j == 1) {
                            g.drawString((String) this.informasiGigi.get(indexGigi), x + (width / 2), y + (width + (width / 2)));
                        }
                    } else if (this.tipeGigi == DiagramGigi.TRANSISI) {
                        g.setColor(Color.blue);
                        List l = (List) this.informasiGigi.get(indexGigi);
                        String index = String.valueOf(l.get(0));
                        g.drawString(index, x + (width / 2), y + (width / 2));

                        g.setColor(Color.black);
                        if (j == 0) {
                            g.drawString((String) l.get(1), x + (width / 2), y - (width / 2));
                        } else if (j == 1) {
                            g.drawString((String) l.get(1), x + (width / 2), y + (width + (width / 2)));
                        }
                    }


                    if (i == this.ruasGigi) {
                        g.setColor(Color.red);
                        g.drawLine((i * width) + XStart, 0, (i * width) + XStart, 2 * ((j * width) + YStart));
                        
                    }

                    if (j == 1) {
                        g.setColor(Color.red);
                        g.drawLine(0, (j * width) + YStart, this.jumlahKolomGigi * width + 40, (j * width) + YStart);
                        
                    }

                    indexGigi++;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setJumlahKolomGigi(int a) {
        this.jumlahKolomGigi = a;
    }

    public void setTipeGigi(int a, String informasi) {
        this.tipeGigi = a;
        this.informasiGigi = new ArrayList();

        if (a == DiagramGigi.DEWASA) {
            this.jumlahKolomGigi = 16;
            this.ruasGigi = 8;
            if (informasi == null) {
                for (int i = 0; i < 32; i++) {
                    informasiGigi.add(i, statusGigiDewasa[0]);
                }
            } else {
                parsingInformasi(informasi);
            }
        } else if (a == DiagramGigi.ANAK) {
            this.jumlahKolomGigi = 10;
            this.ruasGigi = 5;
            if (informasi == null) {
                for (int i = 0; i < 32; i++) {
                    informasiGigi.add(i, statusGigiAnak[0]);
                }
            } else {
                parsingInformasi(informasi);
            }
        } else if (a == DiagramGigi.TRANSISI) {
            this.jumlahKolomGigi = 16;
            this.ruasGigi = 8;


            if (informasi == null) {
                for (int i = 0; i < 32; i++) {
                    List x = new ArrayList();
                    if (i >= 24) {
                        x.add(i - 23);
                    } else if (i >= 16) {
                        x.add(24 - i);

                    } else if (i >= 8) {
                        x.add(i - 7);
                    } else if (i >= 0) {
                        x.add(8 - i);
                    }
                    x.add(statusGigiDewasa[0]);
                    informasiGigi.add(i, x);
                }
            } else {
                parsingInformasi(informasi);
            }
        }
    }

    private void parsingInformasi(String x) {
        String[] str;
        str = x.split(" ");

        if (this.tipeGigi == DiagramGigi.DEWASA || this.tipeGigi == DiagramGigi.ANAK) {
            for (int i = 0; i < str.length; i++) {
                String[] xx = str[i].split("_");
                String hsl = "";
                if(xx.length==2)
                hsl = xx[1];
                
                this.informasiGigi.add(hsl);
            }
        } else {
            for (int i = 0; i < str.length; i++) {
                String[] xx = str[i].split("_");
                 String hsl2 = "";
                if(xx.length==2)
                hsl2 = xx[1];
                
                List lst = new ArrayList();
                lst.add(xx[0]);
                lst.add(hsl2);
                this.informasiGigi.add(lst);
            }
        }

    }

    public void locating() {

        this.XStart = 30;
        this.YStart = 30;
        this.informasiLetakGigi = new ArrayList();
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < this.jumlahKolomGigi; i++) {

                int x = (i * width) + XStart;
                int y = (j * width) + YStart;

                Point p = new Point(x, y);
                this.informasiLetakGigi.add(p);
            }
        }
    }

    public formRecordPasien getParent() {
        return this.parent;
    }

    public void setDialog(statusGigi st) {
        this.dialog = st;
    }

    private static class boxGigiAction implements MouseListener {

        private int indexGigi;
        private DiagramGigi dg;
        private int change;

        public boxGigiAction(int i, DiagramGigi dg) {
            this.indexGigi = i;
            this.dg = dg;
            this.change = 1;
        }

        public void mouseClicked(MouseEvent e) {

            if (this.dg.getTipeGigi() == DiagramGigi.DEWASA) {
                this.dg.informasiGigi.set(this.indexGigi, this.dg.statusGigiDewasa[this.change]);
                this.dg.repaint();
                if (this.change < this.dg.statusGigiDewasa.length - 1) {
                    this.change++;
                } else {
                    this.change = 0;
                }
            } else if (this.dg.getTipeGigi() == DiagramGigi.ANAK) {

                this.dg.informasiGigi.set(this.indexGigi, this.dg.statusGigiAnak[this.change]);
                this.dg.repaint();
                if (this.change < this.dg.statusGigiAnak.length - 1) {
                    this.change++;
                } else {
                    this.change = 0;
                }
            } else if (this.dg.getTipeGigi() == DiagramGigi.TRANSISI) {

                statusGigi st = new statusGigi(this.dg.getParent(), true, this.indexGigi);
                st.setLocationRelativeTo(this.dg.parent);
                st.setVisible(true);

            }
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    private class boxGigi extends JPanel {

        private int indxGigi;

        public boxGigi(int i) {
            this.indxGigi = i;
        }

        public int getIndexGigi() {
            return this.indxGigi;
        }
    }

    private void removeAllBoxGigi() {
        for (int i = 0; i < this.jp.length; i++) {
            this.remove(this.jp[i]);
        }
    }

    public void setInformasiGigi(int index, Object content) {
        this.informasiGigi.set(index, content);
    }

    public char getInformasiTipeGigi() {
        char x = 0;
        if (this.tipeGigi == DiagramGigi.DEWASA) {
            return 'D';
        } else if (this.tipeGigi == DiagramGigi.ANAK) {
            return 'A';
        } else if (this.tipeGigi == DiagramGigi.TRANSISI) {
            return 'T';
        }

        return x;
    }

    public String getInformasiGigi() {
        String r = "";
        if (this.tipeGigi == DiagramGigi.DEWASA || this.tipeGigi == DiagramGigi.ANAK) {
            for (int i = 0; i < this.informasiGigi.size(); i++) {
                r += i + "_" + this.informasiGigi.get(i);
                r += " ";
            }
        } else {
            for (int i = 0; i < this.informasiGigi.size(); i++) {
                List x = new ArrayList();
                x = (List) this.informasiGigi.get(i);
                r += x.get(0) + "_" + x.get(1);
                r += " ";
            }
        }

        return r;
    }
}
