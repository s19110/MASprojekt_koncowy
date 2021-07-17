package Controller;

import Model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa umożliwiająca operacje na problemach poprzez komunikację z bazą danych
 */
public class ProblemController {

    /**
     * Metoda umożliwiająca zapisanie zgłoszonego przez ucznia problemu w bazie danych
     * @param uczen - zgłaszający uczeń
     * @param tresc - treść problemu
     * @param wybranaKlasa - klasa, do której uczeń chce się przenieść
     * @param pracownik - pracownik sekretariatu, do którego zgłaszany jest problem
     */
    public static void zglosProblem(Uczen uczen, String tresc, Klasa wybranaKlasa, PracownikSekretariatu pracownik){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            uczen = session.get(Uczen.class,uczen.getId());
            pracownik = session.get(PracownikSekretariatu.class,pracownik.getId());
            Problem problem = uczen.zglosProblem(tresc,wybranaKlasa,pracownik);

            session.save(uczen);
            session.save(uczen.getOsoba());
            session.save(pracownik);
            session.save(pracownik.getOsoba());
            session.save(problem);

           // if(wybranaKlasa != null)
           //     session.save(wybranaKlasa);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }

    /**
     * Metoda umożliwiająca pobranie wszystkich nierozwiązanych problemów z bazy danych
     * @return - lista problemów
     */
    public static List<Problem> getNierozwiazaneProblemy(){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;
        List<Problem> problemyFromDB = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            problemyFromDB = session.createQuery("from Problem where StatusProblemu=StatusProblemu.NOWY OR StatusProblemu=StatusProblemu.W_TRAKCIE").list();

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
        return problemyFromDB;
    }

    /**
     * Metoda zwracająca listę problemów, które zgłosił dany uczeń
     * @param uczen - uczeń, przez którego zgłoszone problemy chcemy uzyskać
     * @return - lista problemów zgłoszonych przez ucznia
     */
    public static List<Problem> getProblemy(Uczen uczen){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;
        List<Problem> problemyFromDB = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            uczen = session.get(Uczen.class,uczen.getId());
            problemyFromDB = session.createQuery("from Problem where zglaszajacyUczen.id = ?1").setParameter(1,uczen.getId()).list();

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
        return problemyFromDB;
    }

    /**
     * Metoda zwracająca listę problemów, które obsługuje dany pracownik sekretariatu
     * @param pracownik - pracownik sekretariatu, przez którego obsługiwane problemy chcemy uzyskać
     * @return - lista problemów obsługiwanych przez pracownika
     */
    public static List<Problem> getProblemy(PracownikSekretariatu pracownik){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;
        List<Problem> problemyFromDB = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            pracownik = session.get(PracownikSekretariatu.class,pracownik.getId());
            problemyFromDB = session.createQuery("from Problem where obslugujacyPracownik.id = ?1").setParameter(1,pracownik.getId()).list();

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
        return problemyFromDB;
    }

    /**
     * Metoda umożliwiająca pobranie z bazy danych problemów, które zostały przekazane do danego dyrektora
     * @param dyrektor - dyrektor, którego problemy należy pozyskać
     * @return - lista problemów
     */
    public static List<Problem> getProblemy(Nauczyciel dyrektor){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;
        List<Problem> problemyFromDB = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            dyrektor = session.get(Nauczyciel.class,dyrektor.getId());
            problemyFromDB = session.createQuery("from Problem where rozpatrujacyDyrektor.id = ?1").setParameter(1,dyrektor.getId()).list();

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
        return problemyFromDB;
    }

    /**
     * Metoda aktualizująca problem w bazie danych
     * @param problem - problem wymagający aktualizacji
     */
    public static void updateProblem(Problem problem){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;
        List<Problem> problemyFromDB = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            Problem problemFromDB = (Problem) session.get(Problem.class,problem.getId());
            problemFromDB.copyFields(problem);
            if(problem.getRozpatrujacyDyrektor() != null) {
                Nauczyciel dyrektorFromDB = (Nauczyciel) session.get(Nauczyciel.class, problem.getRozpatrujacyDyrektor().getId());
                dyrektorFromDB.setPrzekazaneProblemy(problem.getRozpatrujacyDyrektor().getPrzekazaneProblemy());
                session.save(dyrektorFromDB);
            }
            session.save(problemFromDB);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }



    /**
     * Metoda umożliwiająca zapisanie w bazie danych faktu przekazania problemu do dyrektora
     * @param dyrektor - dyrektor, do którego problem jest przekazywany
     * @param problem - przekazywany problem
     */
    public static void przekazProblemDoDyrektora(Osoba dyrektor, Problem problem){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;
        List<Problem> problemyFromDB = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            dyrektor = (Osoba) session.get(Osoba.class,dyrektor.getId());
            Nauczyciel dyrektorRola = (Nauczyciel) dyrektor.getAktualnaRola();
            problem = (Problem) session.get(Problem.class, problem.getId());

            dyrektorRola.addPrzekazanyProblem(problem);

            session.save(dyrektor);
            session.save(dyrektorRola);
            session.save(problem);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }

    /**
     * Metoda służąca do przenoszenia ucznia zgodnie z informacjami zawartymi w problemie
     * @param problem - problem zawierający informacje o uczniu i klasach
     */
    public static void przeniesUcznia(Problem problem, String noweID){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;
        List<Problem> problemyFromDB = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            Uczen uczen = session.get(Uczen.class,problem.getZglaszajacyUczen().getId());
            Klasa aktualnaKlasa = session.get(Klasa.class,problem.getZglaszajacyUczen().getKlasa().getId());
            Klasa wybranaKlasa = session.get(Klasa.class,problem.getWybranaKlasa().getId());

            aktualnaKlasa.usunUcznia(uczen.getUczenID());
            wybranaKlasa.dodajUcznia(uczen,noweID);

            session.save(uczen);
            session.save(aktualnaKlasa);
            session.save(wybranaKlasa);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }
}
