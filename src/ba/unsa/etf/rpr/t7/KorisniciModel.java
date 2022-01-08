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
    private PreparedStatement sviKorisnici, izmjenaKorisnikaUpit, brisanjeKorisnikaUpit, dodajKorisnikaUpit, dajIdNovogKorisnikaUpit;

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
            izmjenaKorisnikaUpit = connection.prepareStatement("UPDATE korisnik SET ime=?, prezime=?, email=?, username=?, password=? WHERE id=?");
            brisanjeKorisnikaUpit = connection.prepareStatement("DELETE FROM korisnik WHERE id=?");
            dodajKorisnikaUpit = connection.prepareStatement("INSERT INTO korisnik VALUES(?,?,?,?,?,?)");
            dajIdNovogKorisnikaUpit = connection.prepareStatement("SELECT Max(id)+1 FROM korisnik");
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

    public Connection getConnection() { return connection; }

    public void diskonektuj() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
            }
            trenutniKorisnik.set(null);
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
            izmjenaKorisnikaUpit.setString(1, korisnik.getIme());
            izmjenaKorisnikaUpit.setString(2, korisnik.getPrezime());
            izmjenaKorisnikaUpit.setString(3, korisnik.getEmail());
            izmjenaKorisnikaUpit.setString(4, korisnik.getUsername());
            izmjenaKorisnikaUpit.setString(5, korisnik.getPassword());
            izmjenaKorisnikaUpit.setInt(6, korisnik.getId());
            izmjenaKorisnikaUpit.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void obrisi(Korisnik korisnik) {
        try {
            brisanjeKorisnikaUpit.setInt(1, korisnik.getId());
            brisanjeKorisnikaUpit.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dodaj(Korisnik korisnik) {
        try {
            ResultSet resultSet = dajIdNovogKorisnikaUpit.executeQuery();
            int id = 1;
            if(resultSet.next())
                id = resultSet.getInt(1);
            dodajKorisnikaUpit.setInt(1, id);
            dodajKorisnikaUpit.setString(2, korisnik.getIme());
            dodajKorisnikaUpit.setString(3, korisnik.getPrezime());
            dodajKorisnikaUpit.setString(4, korisnik.getEmail());
            dodajKorisnikaUpit.setString(5, korisnik.getUsername());
            dodajKorisnikaUpit.setString(6, korisnik.getPassword());
            dodajKorisnikaUpit.executeUpdate();
            korisnik.setId(id);
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
