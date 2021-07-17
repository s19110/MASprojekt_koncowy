package Utils;

import Model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.Metadata;
import org.hibernate.query.Query;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Klasa służąca do testów na modelu i bazie danych
 */
public class ToTest {
    public static void main(String[] args) {
        fillDB();

    }

    /**
     * Metoda sprawdzająca czy klasa Problem działa poprawnie
     */
    private static void testDzialaniaPrzedmiotow(){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;


        try{
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            Przedmiot przedmiot1 = Przedmiot.utworzPrzedmiotObowiazkowy("Psychologia","Zrozumieć człowieka","Przykładowe wymaganie 1", "Przykładowe wymaganie 2");
            PrzedmiotHumanistyczny typ11 = new PrzedmiotHumanistyczny("Dodatkowa ksiazka");
            PrzedmiotPrzyrodniczy typ12 = new PrzedmiotPrzyrodniczy("Przykladowe narzedzie");

            przedmiot1.dodajTyp(typ11);
            przedmiot1.dodajTyp(typ12);

            session.save(przedmiot1);
            session.save(typ11);
            session.save(typ12);


            Przedmiot przedmiot2 = Przedmiot.utworzPrzedmiotOpcjonalny("Koło matematyczne","Matematyka - podrecznik rozszerzony",
                    LocalTime.of(12,0), DayOfWeek.FRIDAY,   "Zajęcia dodatkowe z matematyki");
            PrzedmiotScisly typ21 = new PrzedmiotScisly("Zbiór zadań", null);
            przedmiot2.dodajTyp(typ21);

            session.save(przedmiot2);
            session.save(typ21);

            session.getTransaction().commit();
            session.close();

            session = sessionFactory.openSession();
            session.beginTransaction();

            System.out.println("===WCZYTANIE PRZEDMIOTÓW Z BAZY===");
            List<Przedmiot> przedmiotyFromDB = session.createQuery("from Przedmiot ").list();

            for(Przedmiot p : przedmiotyFromDB){
                System.out.println(p);
            }
            session.getTransaction().commit();
            session.close();

        }catch (Exception e){
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }finally {
            if(sessionFactory != null){
                sessionFactory.close();
            }
        }
    }

    /**
     * Metoda sprawdzająca czy klasy Osoba, Uczen, PracownikSekretariatu i Nauczyciel działają poprawnie
     */
    private static void testDzialaniaOsob(){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;


        try{
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();


            Osoba uczen1 = new Osoba("123456789","Jan","Kowalski", LocalDate.of(2001,1,1),null,false);
            Osoba pracownik1 = new Osoba("999999999","Piotr","Piotrowski",LocalTime.of(11,0),10);

            session.save(uczen1);
            session.save(uczen1.getAktualnaRola());
            session.save(pracownik1);
            session.save(pracownik1.getAktualnaRola());

            session.getTransaction().commit();
            session.close();

            session = sessionFactory.openSession();
            session.beginTransaction();

            System.out.println("=== WCZYTANIE OSÓB Z BAZY DANYCH ===");

            List<Osoba> osobyFromDB = session.createQuery("from Osoba ").list();

            for (Osoba o : osobyFromDB){
                System.out.println(o);
            }

            List<Uczen> uczniowieFromDB = session.createQuery("from Uczen").list();
            System.out.println("ILOŚĆ UCZNIÓW W BAZIE: " + uczniowieFromDB.size());

            Osoba doZmiany = osobyFromDB.get(0);
            System.out.println("Do zmiany = " + doZmiany);

            Object dawnaRola = doZmiany.zmienNaPracownikaSekretariatu(LocalTime.of(12,0),15);

            session.save(doZmiany);
            session.save(doZmiany.getAktualnaRola());
            session.save(dawnaRola);

            session.getTransaction().commit();
            session.close();

            session = sessionFactory.openSession();
            session.beginTransaction();
            System.out.println("===SPRAWDZENIE, CZY ROLE ZOSTAŁY ZMIENIONE===");
            osobyFromDB = session.createQuery("from Osoba ").list();

            for (Osoba o : osobyFromDB){
                System.out.println(o);
            }
            uczniowieFromDB = session.createQuery("from Uczen").list();
            System.out.println("ILOŚĆ UCZNIÓW W BAZIE: " + uczniowieFromDB.size());

            session.getTransaction().commit();
            session.close();

        }catch (Exception e){
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }finally {
            if(sessionFactory != null){
                sessionFactory.close();
            }
        }
    }

    /**
     * Metoda generująca skrypt SQL do generowania bazy danych i zapisująca go w pliku "create.sql"
     * @param metadata - metadane tworzone przez obiekt StandardServiceRegistry
     */
    private static void logDBCreation(Metadata metadata){
        SchemaExport schemaExport = new SchemaExport();
        schemaExport.setFormat(true);
        schemaExport.setOutputFile("create.sql");
        schemaExport.createOnly(EnumSet.of(TargetType.SCRIPT),metadata);
    }

    /**
     * Metoda sprawdzająca czy klasa Klasa działa poprawnie
     */
    private static void testObslugiKlas(){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;

        try{
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            logDBCreation(metadata);

            Session session = sessionFactory.openSession();
            session.beginTransaction();


            Osoba uczen1 = new Osoba("123456789","Jan","Kowalski", LocalDate.of(2001,1,1),null,false);
            Osoba uczen2 = new Osoba("111222333","Adam","Nowak",LocalDate.of(2002,2,2),null,true);
            Klasa klasa = new Klasa(Klasa.Litera.A, LocalDate.of(2021,1,9));
            klasa.dodajUcznia((Uczen) uczen1.getAktualnaRola(),"A/001");
            klasa.dodajUcznia((Uczen) uczen2.getAktualnaRola(),"A/002");

            System.out.println(((Uczen) uczen1.getAktualnaRola()).getUczenID());
            System.out.println("Klasa = " +uczen1.getKlasa() );

            session.save(uczen1);
            session.save(uczen1.getAktualnaRola());
            session.save(uczen2);
            session.save(uczen2.getAktualnaRola());
            session.save(klasa);

            session.getTransaction().commit();
            session.close();

            session = sessionFactory.openSession();
            session.beginTransaction();

            System.out.println("=== WCZYTANIE KLAS Z BAZY DANYCH ===");

            List<Klasa> klasaFromDB = session.createQuery("from Klasa").list();

            for (Klasa p : klasaFromDB){
                System.out.println(p);
                System.out.println("Uczniowie:" + p.getUczniowie());
                System.out.println("===");
            }

            List<Osoba> osobyFromDb = session.createQuery("from Osoba where uczen is not null ").list();
            for (Osoba p : osobyFromDb){
                System.out.println(p);
                System.out.println("Klasa ucznia = " + p.getKlasa());
                System.out.println("===");
            }

            Query q = session.createQuery("from Klasa where litera =?1");
            q.setParameter(1,Klasa.Litera.A);
            Klasa klasaZKtorejUsuwam = (Klasa) q.uniqueResult();
            Klasa klasa2 = new Klasa(Klasa.Litera.B,LocalDate.of(2022,1,9));
            Uczen uczenDoZmiany = klasaZKtorejUsuwam.usunUcznia("A/002");
            session.save(uczenDoZmiany);
            System.out.println(uczenDoZmiany.getOsoba());
            klasa2.dodajUcznia(uczenDoZmiany,"B/001");

            System.out.println("UCZNIOWIE PO USUNIĘCIU: " +klasaZKtorejUsuwam.getUczniowie());

            session.save(klasaZKtorejUsuwam);
            session.save(klasa2);
            session.save(uczenDoZmiany);

            session.getTransaction().commit();
            session.close();

            session = sessionFactory.openSession();
            session.beginTransaction();
            System.out.println("=== WCZYTANIE UCZNIÓW PO ZMIANIE KLAS Z BAZY DANYCH ===");

            List<Klasa> klasyFromDB2 = session.createQuery("from Klasa").list();
            System.out.println("LICZBA KLAS:" + klasyFromDB2.size());
            for (Klasa p : klasyFromDB2){
                System.out.println(p);
                System.out.println("Klasa ucznia = " + p.getUczniowie());
                System.out.println("===");
            }

            System.out.println("UCZNIOWIE W BAZIE");
            osobyFromDb = session.createQuery("from Osoba where uczen is not null ").list();
            for (Osoba p : osobyFromDb){
                System.out.println(p);
                System.out.println("Klasa ucznia = " + p.getKlasa());
                System.out.println("===");
            }

            Uczen uczenDoUsuniecia = (Uczen) session.createQuery("from Uczen where ukonczylPedagogiczne=false").uniqueResult();
            Osoba osobaDoZmiany = uczenDoUsuniecia.getOsoba();
            Object dawnaRola = uczenDoUsuniecia.getOsoba().zmienNaPracownikaSekretariatu(LocalTime.of(15,0),7);
            session.save(uczenDoUsuniecia);
            session.save(osobaDoZmiany);
            session.save(osobaDoZmiany.getAktualnaRola());
            session.save(uczenDoUsuniecia.getKlasa());

            session.getTransaction().commit();
            session.close();

            session = sessionFactory.openSession();
            session.beginTransaction();
            System.out.println("=== WCZYTANIE KLAS PO ZMIANIE ROLI JEDNEGO Z UCZNIÓW ===");
            klasyFromDB2 = session.createQuery("from Klasa").list();
            System.out.println("LICZBA KLAS:" + klasyFromDB2.size());
            for (Klasa p : klasyFromDB2){
                System.out.println(p);
                System.out.println("Uczniowie w klasie = " + p.getUczniowie());
                System.out.println("===");
            }

            System.out.println("WSZYSTKIE OSOBY W BAZLIE");
            osobyFromDb = session.createQuery("from Osoba").list();
            for (Osoba p : osobyFromDb){
                System.out.println(p);
                System.out.println("Klasa ucznia = " + p.getKlasa());
                System.out.println("===");
            }


            session.getTransaction().commit();
            session.close();

        }catch (Exception e){
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }finally {
            if(sessionFactory != null){
                sessionFactory.close();
            }
        }
    }

    /**
     * Metoda sprawdzająca, czy zgłaszanie problemów działa prawidłowo
     */
    private static void testZglaszaniaProblemow(){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;

        try{
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();


            Osoba uczen1 = new Osoba("123456789","Jan","Kowalski", LocalDate.of(2001,1,1),null,false);
            Osoba pracownik1 = new Osoba("999999999","Piotr","Piotrowski",LocalTime.of(11,0),10);

            Problem problem = ((Uczen) uczen1.getAktualnaRola()).zglosProblem("ABCD",null,(PracownikSekretariatu)pracownik1.getAktualnaRola());
            session.save(uczen1);
            session.save(uczen1.getAktualnaRola());
            session.save(pracownik1);
            session.save(pracownik1.getAktualnaRola());
            session.save(problem);

            session.getTransaction().commit();
            session.close();

            session = sessionFactory.openSession();
            session.beginTransaction();

            System.out.println("=== WCZYTANIE PROBLEMÓW Z BAZY DANYCH ===");

            List<Problem> problemyFromDB = session.createQuery("from Problem ").list();

            for (Problem p : problemyFromDB){
                System.out.println(p);
                System.out.println("===");
            }

            session.getTransaction().commit();
            session.close();

        }catch (Exception e){
        e.printStackTrace();
        StandardServiceRegistryBuilder.destroy(registry);
        }finally {
            if(sessionFactory != null){
                sessionFactory.close();
        }
    }
    }

    /**
     * Metoda zapełniająca bazę danych danymi wstępnymi
     */
    private static void fillDB(){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;


        try{
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            List<Osoba> wszystkieOsoby = new ArrayList<>();
            List<Klasa> wszystkieKlasy = new ArrayList<>();
            Osoba uczen1 = new Osoba("123456789","Jan","Kowalski", LocalDate.of(2001,1,1),null,false);
            Osoba uczen2 = new Osoba("999999999","Piotr","Piotrowski", LocalDate.of(1999,1,1),LocalDate.of(2016,12,1),false);
            Osoba uczen3 = new Osoba("111111111","Anna","Nowak", LocalDate.of(1999,2,14),LocalDate.of(2016,12,1),true);
            Osoba pracownik1 = new Osoba("888888888","Konrad","Piekarczyk",LocalTime.of(8,0),10);
            Osoba pracownik2 = new Osoba("456456456","Zuzanna","Róża",LocalTime.of(11,0),10);
            Osoba nauczyciel1 = new Osoba("100000000","Julian","Król",LocalDate.of(2013,5,6),5000,true);
            Osoba nauczyciel2 = new Osoba("100000001","Julia","Nowa",LocalDate.of(2013,5,6),5000,false);
            Klasa klasa1 = new Klasa(Klasa.Litera.A, LocalDate.of(2021,1,9));
            Klasa klasa2 = new Klasa(Klasa.Litera.B, LocalDate.of(2021,1,9));
            Klasa klasa3 = new Klasa(Klasa.Litera.C,LocalDate.of(2020,1,9));
            Klasa klasa4 = new Klasa(Klasa.Litera.D, LocalDate.of(2021,1,9));
            klasa1.dodajUcznia( (Uczen) uczen1.getAktualnaRola(),"A/001");
            klasa1.dodajUcznia((Uczen) uczen2.getAktualnaRola(),"A/002");
            klasa2.dodajUcznia((Uczen) uczen3.getAktualnaRola(), "B/001");

            for(int i = 0; i< 30 ; i++){
                Osoba fillerStudent = new Osoba(Integer.toString(i),"Wypełnienie", "Klasy 1D",LocalDate.of(1,1,1),null,false);
                klasa4.dodajUcznia((Uczen) fillerStudent.getAktualnaRola(),"D/"+Integer.toString(i));
                wszystkieOsoby.add(fillerStudent);
            }

            wszystkieOsoby.add(uczen1);
            wszystkieOsoby.add(uczen2);
            wszystkieOsoby.add(uczen3);
            wszystkieOsoby.add(pracownik1);
            wszystkieOsoby.add(pracownik2);
            wszystkieOsoby.add(nauczyciel1);
            wszystkieOsoby.add(nauczyciel2);

            wszystkieKlasy.add(klasa1);
            wszystkieKlasy.add(klasa2);
            wszystkieKlasy.add(klasa3);
            wszystkieKlasy.add(klasa4);

            for(Osoba o : wszystkieOsoby){
                session.save(o);
                session.save(o.getAktualnaRola());
            }
            for (Klasa k : wszystkieKlasy){
                session.save(k);
            }

            session.getTransaction().commit();
            session.close();

        }catch (Exception e){
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }finally {
            if(sessionFactory != null){
                sessionFactory.close();
            }
        }
    }
}
