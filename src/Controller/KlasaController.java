package Controller;

import Model.Klasa;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Klasa odpowiedzialna za komunikację z bazą danych w przypadku operacjach insert/alter/delete na klasach szkolnych
 */
public class KlasaController {

    public static boolean czyIDJestZajete(Klasa klasa, String id){
        return klasa.getUczniowie().keySet().contains(id);
    }

    public static void saveKlasa(Klasa klasa){
        if(klasa == null)
            return;

            StandardServiceRegistry registry = null;
            SessionFactory sessionFactory = null;
            try {
                registry = new StandardServiceRegistryBuilder().configure().build();
                Metadata metadata = new MetadataSources(registry).buildMetadata();
                sessionFactory = metadata.buildSessionFactory();

                Session session = sessionFactory.openSession();
                session.beginTransaction();

                session.save(klasa);

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
