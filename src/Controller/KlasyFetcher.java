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
 * Klasa do kontaktu z bazą danych w przypadku, gdy chcemy pobrać klasy szkolne z bazy
 */
public class KlasyFetcher {

    /**
     * Zwraca listę klas szkolnych pobranych z bazy danych
     * @return - lista pobranych klas
     */
    public static List<Klasa> getKlasy() {
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;
        List<Klasa> klasyFromDB = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            klasyFromDB = session.createQuery("from Klasa ").list();


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
        return klasyFromDB;
    }
}
