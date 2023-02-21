package com.example.ea_java_2.repository;

import com.example.ea_java_2.exception.CustomException;

import java.util.List;

/**
 * Generic repository to implement CRUD
 *
 * @param <T> is any type of record
 * @param <ID> is the type of primary key in the DAO (Primary Key)
 */
public interface CRUDRepository<T, ID> {
    /**
     * Return all occurances of T.
     *
     * @return List<T>
     */
    List<T> findAll() throws CustomException;

    /**
     * Return T matching the provided id.
     *
     * @param id
     * @return matching T
     */
    T findById(ID id) throws CustomException;

    /**
     * Create new object of type T.
     *
     * @param object
     * @return number of affected rows
     */
    int add (T object) throws CustomException;

    /**
     * Update an existing object of type T.
     *
     * @param object
     * @return number of affected rows.
     */
    int update (T object) throws CustomException;
}
