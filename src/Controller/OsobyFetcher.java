package Controller;

import Model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa do kontaktu z bazą danych w przypadku aktywnościach związanych z osobami
 */
public class OsobyFetcher {
    public enum Role {UCZEN,DYREKTORZY,NAUCZYCIELE,PRACOWNIK}

    /**
     * Metoda zwracająca wszystkie osoby z bazy danych pełniące wybraną rolę
     * @param wybranaRola - rola osób, które należy pobrać z bazy danych
     * @return lista osób o podanej roli
     */
    public static List<Osoba> getOsoby(Role wybranaRola) {
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;
        List<Osoba> osobyFromDB = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            Metadata metadata = new MetadataSources(registry).buildMetadata();
            sessionFactory = metadata.buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            System.out.println("PRZED ZAPYTANIEM");

            if(wybranaRola == Role.UCZEN){
                List<Uczen> uczniowie = session.createQuery(" from Uczen ").list();
                //Sorted zapewnia ograniczenie {ordered} z diagramu}
                osobyFromDB = uczniowie.stream().map(Uczen::getOsoba).sorted(Comparator.comparing(Osoba::getNazwisko)).collect(Collectors.toList());
            }
            else if(wybranaRola == Role.PRACOWNIK){
                List<PracownikSekretariatu> pracownicy = session.createQuery(" from PracownikSekretariatu ").list();
                osobyFromDB = pracownicy.stream().map(PracownikSekretariatu::getOsoba).collect(Collectors.toList());
            }
            else if(wybranaRola == Role.DYREKTORZY){
                List<Nauczyciel> pracownicy = session.createQuery(" from Nauczyciel where czyDyrektor = true").list();
                osobyFromDB = pracownicy.stream().map(Nauczyciel::getOsoba).collect(Collectors.toList());
            }
            else if(wybranaRola == Role.NAUCZYCIELE){
                List<Nauczyciel> pracownicy = session.createQuery(" from Nauczyciel").list();
                osobyFromDB = pracownicy.stream().map(Nauczyciel::getOsoba).collect(Collectors.toList());
            }
//            System.out.println("POBRANE OSOBY");
//            for(Osoba o : osobyFromDB){
//                System.out.println(o);
//            }

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
        return osobyFromDB;
    }

}
