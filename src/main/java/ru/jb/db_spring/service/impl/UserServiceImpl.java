package ru.jb.db_spring.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Service;
import ru.jb.db_spring.domain.Note;
import ru.jb.db_spring.domain.User;
import ru.jb.db_spring.service.api.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final EntityManagerFactory emf;

    public UserServiceImpl(EntityManagerFactory emf) {this.emf = emf;}

    @Override
    public void createUserWithNotes() {
        var em = emf.createEntityManager();
        var transaction = em.getTransaction();
        try {
            //создание пользователя со связанными статьями
            transaction.begin();
            User u = new User();
            u.setUsername("random_username");

            Note n1 = new Note();
            n1.setText("Статья 1");
            u.addNote(n1);

            Note n2 = new Note();
            n2.setText("Статья 2");
            u.addNote(n2);

            Note n3 = new Note();
            n3.setText("Статья 3");
            u.addNote(n3);

            //сохранение пользователя в бд
            em.persist(u);
            transaction.commit();

            //очистка контекста em
            em.clear();

            //проверка, что будет sql в логах по запросу
            transaction.begin();
            User loadedUser = em.createQuery("select u from User u where u.username = :un", User.class)
                                .setParameter("un", "random_username")
                                .getSingleResult();

            System.out.println("Loaded user: id=" + loadedUser.getId() + ", username=" + loadedUser.getUsername());
            //lazy инициализация - будет селект по заметкам
            System.out.println("Notes count = " + loadedUser.getNotes().size());
            transaction.commit();

            transaction.begin();
            User userToGetNotes = em.createQuery("select u from User u where u.username = :un", User.class)
                                    .setParameter("un", "random_username")
                                    .getSingleResult();
            em.remove(userToGetNotes.getNotes().get(0));
            transaction.commit();

            transaction.begin();
            User userToDelete = em.createQuery("select u from User u where u.username = :un", User.class)
                                    .setParameter("un", "random_username")
                                    .getSingleResult();
            em.remove(userToDelete);
            transaction.commit();
        } finally {
            em.close();
        }
    }
}
