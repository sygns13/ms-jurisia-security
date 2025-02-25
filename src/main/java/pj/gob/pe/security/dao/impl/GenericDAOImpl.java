package pj.gob.pe.security.dao.impl;

import pj.gob.pe.security.dao.GenericDAO;
import pj.gob.pe.security.repository.GenericRepo;

import java.util.List;

public abstract class GenericDAOImpl<T, ID> implements GenericDAO<T, ID> {

    protected abstract GenericRepo<T, ID> getRepo();

    @Override
    public T registrar(T t) throws Exception {
        return getRepo().save(t);
    }

    @Override
    public T modificar(T t) throws Exception {
        return getRepo().save(t);
    }

    @Override
    public List<T> listar() throws Exception {
        return getRepo().findAll();
    }

    @Override
    public T listarPorId(ID id) throws Exception {
        return getRepo().findById(id).orElse(null);
    }

    @Override
    public void eliminar(ID id) throws Exception {
        getRepo().deleteById(id);
    }
}
