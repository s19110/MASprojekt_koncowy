package Controller;

import Model.OcenaCzastkowa;
import Model.OcenaSemestralna;
import Model.Problem;
import Model.Uczen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class OcenyController {

    /**
     * Metoda pozwalająca pobrać wszystkie oceny cząstkowe z bazy danych
     * @return - lista ocen cząstkowych
     */
    public static List<OcenaCzastkowa> getOcenyCzastkowe(){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;
        List<OcenaCzastkowa> ocenyFromDB = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            ocenyFromDB = session.createQuery("from OcenaCzastkowa ").list();

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
        return ocenyFromDB;
    }

    /**
     * Metoda pozwalająca pobrać wszystkie oceny semestralne z bazy danych
     * @return - lista ocen semestralnych
     */
    public static List<OcenaSemestralna> getOcenySemestralne(){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;
        List<OcenaSemestralna> ocenyFromDB = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            ocenyFromDB = session.createQuery("from OcenaSemestralna ").list();

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
        return ocenyFromDB;
    }

    /**
     * Metoda pozwalająca pobrać wszystkie oceny semestralne, które dany uczeń uzyskał w danym roku
     * @param uczen - uczeń, którego oceny chcemy pobrać
     * @param rok - rok, w którym poszukiwane oceny zostały wystawione
     * @return - lista ocen semestralnych
     */
    public static List<OcenaSemestralna> getOcenySemestralne(Uczen uczen, int rok){
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;
        List<OcenaSemestralna> ocenyFromDB = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            ocenyFromDB = session.createQuery("from OcenaSemestralna where uczen.id = ?1").setParameter(1,uczen.getId()).list();
            ocenyFromDB = ocenyFromDB.stream().filter(ocenaSemestralna -> ocenaSemestralna.getDataWystawienia().getYear() == rok).collect(Collectors.toList());

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
        return ocenyFromDB;
    }
}
