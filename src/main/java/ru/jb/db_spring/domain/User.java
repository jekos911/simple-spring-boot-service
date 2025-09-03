package ru.jb.db_spring.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users",
       indexes = {
               @Index(name = "ux_users_username", columnList = "username", unique = true)
       }
)
public class User {

       @Id
       @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
       @SequenceGenerator(name = "user_seq", sequenceName = "user_seq")
       private Long id;

       @Column(nullable = false, unique = true, length = 30)
       @Size(min = 3, max = 30)
       @Pattern(regexp = "^[A-Za-z0-9_]+$")
       private String username;

       @OneToMany(
               mappedBy = "author",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY
       )
       private List<Note> notes = new ArrayList<>();

       @OneToMany(
               mappedBy = "user",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY
       )
       private List<Product> products = new ArrayList<>();

       public void addNote(Note note) {
              notes.add(note);
              note.setAuthor(this);
       }

       public void removeNote(Note note) {
              notes.remove(note);
              note.setAuthor(null);
       }

       public void addProduct(Product product) {
              products.add(product);
              product.setUser(this);
       }

       public void removeProduct(Product product) {
              products.remove(product);
              product.setUser(null);
       }


       public Long getId() {
              return id;
       }

       public void setId(Long id) {
              this.id = id;
       }

       public String getUsername() {
              return username;
       }

       public void setUsername(String username) {
              this.username = username;
       }

       public List<Note> getNotes() {
              return notes;
       }

       public void setNotes(List<Note> notes) {
              this.notes = notes;
       }

       @Override
       public boolean equals(Object o) {
              if (this == o) {
                     return true;
              }
              if (o == null || getClass() != o.getClass()) {
                     return false;
              }
              User user = (User) o;
              return Objects.equals(id, user.id) && Objects.equals(username,
                                                                   user.username) && Objects.equals(
                      notes, user.notes);
       }

       @Override
       public int hashCode() {
              return Objects.hash(id, username, notes);
       }
}
