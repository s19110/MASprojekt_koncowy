package Controller;

import Model.PrzedmiotHumanistyczny;
import Model.PrzedmiotPrzyrodniczy;
import Model.PrzedmiotScisly;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

/**
 * Metoda umożliwiająca pobranie przedmiotów z bazy danych
 */
public class PrzedmiotyFetcher {

    /**
     * Metoda umożliwiająca pobranie przedmiotów humanistycznych z bazy danych
     * @return - lista przedmiotów humanistycznych
     */
    public static List<PrzedmiotHumanistyczny> getPrzedmiotyHumanistyczne(){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;
        List<PrzedmiotHumanistyczny> przedmiotyFromDB = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            przedmiotyFromDB = session.createQuery("from PrzedmiotHumanistyczny ").list();

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
        return przedmiotyFromDB;
    }

    /**
     * Metoda umożliwiająca pobranie przedmiotów przyrodniczych z bazy danych
     * @return - lista przedmiotów przyrodniczych
     */
    public static List<PrzedmiotPrzyrodniczy> getPrzedmiotyPrzyrodnicze(){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;
        List<PrzedmiotPrzyrodniczy> przedmiotyFromDB = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            przedmiotyFromDB = session.createQuery("from PrzedmiotPrzyrodniczy ").list();

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
        return przedmiotyFromDB;
    }

    /**
     * Metoda umożliwiająca pobranie przedmiotów ścisłych z bazy danych
     * @return - lista przedmiotów ścisłych
     */
    public static List<PrzedmiotScisly> getPrzedmiotyScisle(){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;
        List<PrzedmiotScisly> przedmiotyFromDB = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            przedmiotyFromDB = session.createQuery("from PrzedmiotScisly ").list();

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
        return przedmiotyFromDB;
    }
}
