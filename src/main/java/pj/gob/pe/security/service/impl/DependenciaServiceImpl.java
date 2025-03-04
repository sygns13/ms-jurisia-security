package pj.gob.pe.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pj.gob.pe.security.dao.mysql.DependenciaDAO;
import pj.gob.pe.security.exception.ValidationServiceException;
import pj.gob.pe.security.model.entities.Dependencia;
import pj.gob.pe.security.service.DependenciaService;
import pj.gob.pe.security.utils.Constantes;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DependenciaServiceImpl implements DependenciaService {

    private final DependenciaDAO dependenciaDAO;

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void altabaja(Long id, Integer valor) throws Exception {

        Dependencia dependenciaDB = dependenciaDAO.listarPorId(id);
        LocalDateTime fechaActualTime = LocalDateTime.now();

        dependenciaDB.setUpdDate(fechaActualTime.toLocalDate());
        dependenciaDB.setUpdDatetime(fechaActualTime);
        dependenciaDB.setUpdTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        //Oauth inicio
        dependenciaDB.setUpdUserId(1L);
        //Oauth final

        dependenciaDB.setActivo(valor);

        dependenciaDAO.modificar(dependenciaDB);
    }

    @Override
    public Page<Dependencia> listar(Pageable pageable, String buscar, Long dependenciaId) throws Exception {
        Map<String, Object> filters = new HashMap<>();
        filters.put("or_codigo", buscar);
        filters.put("or_nombre", buscar);
        filters.put("borrado", 0);

        if(dependenciaId != null && dependenciaId > 0L){
            filters.put("dependenciaId", dependenciaId);
        }

        Map<String, Object> notEqualFilters = new HashMap<>();

        return dependenciaDAO.findDependenciasByFiltersPage(filters, notEqualFilters, pageable);
    }

    @Override
    public Page<Dependencia> listar(Pageable pageable, String buscar) throws Exception {
        return this.listar(pageable, buscar, null);
    }

    @Override
    public Dependencia registrar(Dependencia dependencia) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        dependencia.setRegDate(fechaActualTime.toLocalDate());
        dependencia.setRegDatetime(fechaActualTime);
        dependencia.setRegTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        if(dependencia.getDependenciaId() == null || dependencia.getDependenciaId() <= 0){
            dependencia.setDependenciaId(null);
        }

        //Oauth inicio
        dependencia.setRegUserId(1L);
        //Oauth final

        dependencia.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        dependencia.setActivo(Constantes.REGISTRO_ACTIVO);

        if(dependencia.getCodigo() == null) dependencia.setCodigo(Constantes.VOID_STRING);
        if(dependencia.getNombre() == null) dependencia.setNombre(Constantes.VOID_STRING);
        if(dependencia.getSigla() == null) dependencia.setSigla(Constantes.VOID_STRING);

        dependencia.setCodigo(dependencia.getCodigo().trim());
        dependencia.setNombre(dependencia.getNombre().trim());
        dependencia.setSigla(dependencia.getSigla().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(dependencia, resultValidacion);

        if(validacion)
            return this.grabarRegistro(dependencia);

        String errorValidacion = "Error de validación Método Registrar Dependencia";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }


        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public int modificar(Dependencia dependenciaEdit) throws Exception {

        Dependencia dependencia = dependenciaDAO.listarPorId(dependenciaEdit.getId());

        dependencia.setCodigo(dependenciaEdit.getCodigo());
        dependencia.setNombre(dependenciaEdit.getNombre());
        dependencia.setSigla(dependenciaEdit.getSigla());

        LocalDateTime fechaActualTime = LocalDateTime.now();
        dependencia.setUpdDate(fechaActualTime.toLocalDate());
        dependencia.setUpdDatetime(fechaActualTime);
        dependencia.setUpdTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        if(dependencia.getDependenciaId() == null || dependencia.getDependenciaId() <= 0){
            dependencia.setDependenciaId(null);
        }

        //Oauth inicio
        dependencia.setUpdUserId(1L);
        //Oauth final

        if(dependencia.getCodigo() == null) dependencia.setCodigo(Constantes.VOID_STRING);
        if(dependencia.getNombre() == null) dependencia.setNombre(Constantes.VOID_STRING);
        if(dependencia.getSigla() == null) dependencia.setSigla(Constantes.VOID_STRING);

        dependencia.setCodigo(dependencia.getCodigo().trim());
        dependencia.setNombre(dependencia.getNombre().trim());
        dependencia.setSigla(dependencia.getSigla().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(dependencia, resultValidacion);

        if(validacion)
            return this.grabarRectificar(dependencia);

        String errorValidacion = "Error de validación Método Modificar Dependencia";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }


        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public List<Dependencia> listar() throws Exception {
        Map<String, Object> filters = new HashMap<>();
        filters.put("borrado", 0);

        Map<String, Object> notEqualFilters = new HashMap<>();

        return dependenciaDAO.findDependenciasByFiltersV2(filters, notEqualFilters);
    }

    @Override
    public Dependencia listarPorId(Long id) throws Exception {
        //return dependenciaDAO.listarPorId(id);

        Map<String, Object> filters = new HashMap<>();
        filters.put("id", id);
        filters.put("borrado", 0);

        Map<String, Object> notEqualFilters = new HashMap<>();

        List<Dependencia> dependencias = dependenciaDAO.findDependenciasByFiltersV2(filters, notEqualFilters);

        if(dependencias.size() > 0)
            return dependencias.get(0);
        else
            return null;

    }

    @Override
    public void eliminar(Long id) throws Exception {
        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionEliminacion(id, resultValidacion);

        if(validacion) {
            this.grabarEliminar(id);
        }
        else {
            String errorValidacion = "Error de validación Método Eliminar Dependencia";

            if (resultValidacion.get("errors") != null) {
                List<String> errors = (List<String>) resultValidacion.get("errors");
                if (errors.size() > 0)
                    errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
            }

            throw new ValidationServiceException(errorValidacion);
        }
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public Dependencia grabarRegistro(Dependencia dependencia) throws Exception {
        return dependenciaDAO.registrar(dependencia);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(Dependencia dependencia) throws Exception {

        dependenciaDAO.modificar(dependencia);
        return Constantes.CANTIDAD_UNIDAD_INTEGER;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void grabarEliminar(Long id) throws Exception {

        Dependencia dependenciaDB = dependenciaDAO.listarPorId(id);
        LocalDateTime fechaActualTime = LocalDateTime.now();

        dependenciaDB.setUpdDate(fechaActualTime.toLocalDate());
        dependenciaDB.setUpdDatetime(fechaActualTime);
        dependenciaDB.setUpdTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        //Oauth inicio
        dependenciaDB.setUpdUserId(1L);
        //Oauth final

        dependenciaDB.setBorrado(Constantes.REGISTRO_BORRADO);

        dependenciaDAO.modificar(dependenciaDB);
    }

    @Override
    public boolean validacionRegistro(Dependencia dependencia, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(dependencia.getCodigo() == null || dependencia.getCodigo().isEmpty()){
            resultado = false;
            error = "Ingrese el Código del Dependencia";
            errors.add(error);
        }

        if(dependencia.getNombre() == null || dependencia.getNombre().isEmpty()){
            resultado = false;
            error = "Ingrese el Nombre del Dependencia";
            errors.add(error);
        }

        Map<String, Object> filters = new HashMap<>();
        filters.put("or_codigo", dependencia.getCodigo());
        filters.put("or_nombre", dependencia.getNombre());
        filters.put("borrado", 0);

        Map<String, Object> notEqualFilters = new HashMap<>();

        List<Dependencia> dependencias = dependenciaDAO.findDependenciasByFiltersV2(filters, notEqualFilters);

        if(!dependencias.isEmpty()){
            resultado = false;
            error = "El código o el Nombre del Dependencia ingresado ya se encuentra registrado";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(Dependencia dependencia, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(dependencia.getId() == null){
            resultado = false;
            error = "Ingrese el Id del Dependencia";
            errors.add(error);
        }

        if(dependencia.getCodigo() == null || dependencia.getCodigo().isEmpty()){
            resultado = false;
            error = "Ingrese el Código del Dependencia";
            errors.add(error);
        }

        if(dependencia.getNombre() == null || dependencia.getNombre().isEmpty()){
            resultado = false;
            error = "Ingrese el Nombre del Dependencia";
            errors.add(error);
        }

        Map<String, Object> filters = new HashMap<>();
        filters.put("or_codigo", dependencia.getCodigo());
        filters.put("or_nombre", dependencia.getNombre());
        filters.put("borrado", 0);

        Map<String, Object> notEqualFilters = new HashMap<>();
        notEqualFilters.put("id", dependencia.getId());

        List<Dependencia> dependencias = dependenciaDAO.findDependenciasByFiltersV2(filters, notEqualFilters);

        if(!dependencias.isEmpty()){
            resultado = false;
            error = "El código o el Nombre del Dependencia ingresado ya se encuentra registrado";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionEliminacion(Long id, Map<String, Object> resultValidacion) throws Exception {

        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        //Lógica de Validaciones para Eliminación Producto

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }
}
