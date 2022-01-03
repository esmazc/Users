package ba.unsa.etf.rpr.t7;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class KorisniciModel {
    private ObservableList<Korisnik> korisnici = FXCollections.observableArrayList();
    private SimpleObjectProperty<Korisnik> trenutniKorisnik = new SimpleObjectProperty<>();

    private Connection connection;
    private PreparedStatement sviKorisnici, izmjenaUpit, brisanjeUpit;

    public KorisniciModel() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:korisnici.db");
        } catch(SQLException e) {
            e.printStackTrace();
        }
        try {
            sviKorisnici = connection.prepareStatement("SELECT * FROM korisnik");
        } catch(SQLException e) {
            regenerisiBazu();
            try {
                sviKorisnici = connection.prepareStatement("SELECT * FROM korisnik");
            } catch(SQLException ex) {
                ex.printStackTrace();
            }
        }
        try {
            izmjenaUpit = connection.prepareStatement("UPDATE korisnik SET ime=?, prezime=?, email=?, username=?, password=? WHERE id=?");
            brisanjeUpit = connection.prepareStatement("DELETE FROM korisnik WHERE id=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void regenerisiBazu() {
        Scanner ulaz;
        try {
            ulaz = new Scanner(new FileInputStream("korisnici.db.sql"));
            String sqlUpit = "";
            while(ulaz.hasNext()) {
                sqlUpit += ulaz.nextLine();
                if(sqlUpit.length() > 1 && sqlUpit.charAt(sqlUpit.length()-1) == ';') {
                    try {
                        Statement stmt = connection.createStatement();
                        stmt.execute(sqlUpit);
                        sqlUpit = "";
                    } catch(SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            ulaz.close();
        } catch(FileNotFoundException e) {
            System.out.println("Ne postoji SQL datoteka... nastavljam sa praznom bazom");
        }
    }

    public void napuni() {
        // Ako je lista već bila napunjena, praznimo je
        // Na taj način se metoda napuni() može pozivati više puta u testovima
        korisnici.clear();
        try {
            ResultSet rs = sviKorisnici.executeQuery();
            while (rs.next()) {
                Korisnik korisnik = new Korisnik(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
                korisnici.add(korisnik);
                if (trenutniKorisnik == null) trenutniKorisnik = new SimpleObjectProperty<>(korisnik);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*korisnici.add(new Korisnik("Vedran", "Ljubović", "vljubovic@etf.unsa.ba", "vedranlj", "test"));
        korisnici.add(new Korisnik("Amra", "Delić", "adelic@etf.unsa.ba", "amrad", "test"));
        korisnici.add(new Korisnik("Tarik", "Sijerčić", "tsijercic1@etf.unsa.ba", "tariks", "test"));
        korisnici.add(new Korisnik("Rijad", "Fejzić", "rfejzic1@etf.unsa.ba", "rijadf", "test"));
        trenutniKorisnik.set(null);*/
    }

    public void izmijeni(Korisnik korisnik) {
        try {
            izmjenaUpit.setString(1, korisnik.getIme());
            izmjenaUpit.setString(2, korisnik.getPrezime());
            izmjenaUpit.setString(3, korisnik.getEmail());
            izmjenaUpit.setString(4, korisnik.getUsername());
            izmjenaUpit.setString(5, korisnik.getPassword());
            izmjenaUpit.setInt(6, korisnik.getId());
            izmjenaUpit.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void obrisi(Korisnik korisnik) {
        try {
            brisanjeUpit.setInt(1, korisnik.getId());
            brisanjeUpit.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void vratiNaDefault() {
        // Dodali smo metodu vratiNaDefault koja trenutno ne radi ništa, a kada prebacite Model na DAO onda
        // implementirajte ovu metodu
        // Razlog za ovo je da polazni testovi ne bi padali nakon što dodate bazu
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM korisnik");
            regenerisiBazu();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void diskonektuj() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Korisnik> getKorisnici() {
        return korisnici;
    }

    public void setKorisnici(ObservableList<Korisnik> korisnici) {
        this.korisnici = korisnici;
    }

    public Korisnik getTrenutniKorisnik() {
        return trenutniKorisnik.get();
    }

    public SimpleObjectProperty<Korisnik> trenutniKorisnikProperty() {
        return trenutniKorisnik;
    }

    public void setTrenutniKorisnik(Korisnik trenutniKorisnik) {
        if(this.trenutniKorisnik.getValue() != null) izmijeni(this.trenutniKorisnik.getValue());
        this.trenutniKorisnik.set(trenutniKorisnik);
    }

    public void setTrenutniKorisnik(int i) {
        if(this.trenutniKorisnik.getValue() != null) izmijeni(this.trenutniKorisnik.getValue());
        this.trenutniKorisnik.set(korisnici.get(i));
    }

    public void zapisiDatoteku(File file) {
        if(file == null) return;
        PrintWriter izlaz;
        try {
            izlaz = new PrintWriter(new FileWriter(file));
            for(Korisnik korisnik : korisnici) {
                izlaz.println(korisnik.getUsername() + ":" + korisnik.getPassword() + ":" + korisnik.getId() + ":" + korisnik.getId() + ":"
                        + korisnik.getIme() + " " + korisnik.getPrezime() + "::");
            }
            izlaz.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
