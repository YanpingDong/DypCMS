package com.dyp.repository;

import com.dyp.entity.Person;
import com.dyp.entity.PersonRole;

import java.util.List;

public interface IPersonRole {

    public Person getPersonAndRoleByUid(int uId);

    public void insertPerson(Person person);

    public void queryPersonById(int id);

    public void deletePersonById(int id);

    public void insertPersonRole(List<PersonRole> personRoles);
}
