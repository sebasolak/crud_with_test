package com.sebastian.learningspringboot.dao;

import com.sebastian.learningspringboot.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sebastian.learningspringboot.model.User.Gender.*;
import static org.assertj.core.api.Assertions.assertThat;


public class FakeDataDaoTest {

    private FakeDataDao fakeDataDao;


    @Before
    public void setUp() throws Exception {
        fakeDataDao = new FakeDataDao();
    }

    @Test
    public void selectAllUsers() {
        List<User> users = fakeDataDao.selectAllUsers();
        assertThat(users).hasSize(1);

        User user = users.get(0);

        assertThat(user.getAge()).isEqualTo(22);
        assertThat(user.getFirstName()).isEqualTo("Joe");
        assertThat(user.getLastName()).isEqualTo("Jones");
        assertThat(user.getGender()).isEqualTo(MALE);
        assertThat(user.getEmail()).isEqualTo("joe.jones@gmail.com");
        assertThat(user.getUserUid()).isNotNull();
    }

    @Test
    public void shouldSelectUserByUserUid() {
        UUID annaUserUid = UUID.randomUUID();
        User anna = new User(annaUserUid,
                "Anna", "Montana",
                FEMALE, 30, "anna@gmail.com");
        fakeDataDao.insertUser(annaUserUid,anna);
        assertThat(fakeDataDao.selectAllUsers()).hasSize(2);

        Optional<User> annaOptional =
                fakeDataDao.selectUserByUserUid(annaUserUid);
        assertThat(annaOptional.isPresent()).isTrue();
        assertThat(annaOptional.get())
                .isEqualToComparingFieldByField(anna);
    }

    @Test
    public void shouldNotSelectUserByRandomUserUid() {
        Optional<User> user =
                fakeDataDao.selectUserByUserUid(UUID.randomUUID());
        assertThat(user.isPresent()).isFalse();
    }

    @Test
    public void shouldUpdateUser() {
        UUID joeUserUid
                = fakeDataDao.selectAllUsers().get(0).getUserUid();
        User newJoe = new User(joeUserUid,
                "Anna", "Montana",
                FEMALE, 30, "anna@gmail.com");

        fakeDataDao.updateUser(newJoe);

        Optional<User> user =
                fakeDataDao.selectUserByUserUid(joeUserUid);
        assertThat(user.isPresent()).isTrue();

        assertThat(fakeDataDao.selectAllUsers()).hasSize(1);
        assertThat(user.get()).isEqualToComparingFieldByField(newJoe);
    }

    @Test
    public void deleteUserByUserUid() {
        UUID joeUserUid
                = fakeDataDao.selectAllUsers().get(0).getUserUid();

        fakeDataDao.deleteUserByUserUid(joeUserUid);

        assertThat(fakeDataDao.selectUserByUserUid(joeUserUid).isPresent()).isFalse();
        assertThat(fakeDataDao.selectAllUsers()).isEmpty();
    }

    @Test
    public void insertUser() {
        UUID userUid = UUID.randomUUID();
        User user = new User(userUid,
                "Anna", "Montana",
                FEMALE, 30, "anna@gmail.com");

        fakeDataDao.insertUser(userUid, user);

        List<User> users = fakeDataDao.selectAllUsers();
        assertThat(users).hasSize(2);
        assertThat(fakeDataDao.selectUserByUserUid(userUid).get()).isEqualToComparingFieldByField(user);
    }
}