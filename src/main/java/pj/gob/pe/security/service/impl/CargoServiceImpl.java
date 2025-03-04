package pj.gob.pe.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pj.gob.pe.security.dao.mysql.CargoDAO;
import pj.gob.pe.security.exception.ValidationServiceException;
import pj.gob.pe.security.model.entities.Cargo;
import pj.gob.pe.security.service.CargoService;
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
public class CargoServiceImpl implements CargoService {

    private final CargoDAO cargoDAO;

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void altabaja(Long id, Integer valor) throws Exception {

        Cargo cargoDB = cargoDAO.listarPorId(id);
        LocalDateTime fechaActualTime = LocalDateTime.now();

        cargoDB.setUpdDate(fechaActualTime.toLocalDate());
        cargoDB.setUpdDatetime(fechaActualTime);
        cargoDB.setUpdTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        //Oauth inicio
        cargoDB.setUpdUserId(1L);
        //Oauth final

        cargoDB.setActivo(valor);

        cargoDAO.modificar(cargoDB);
    }

    @Override
    public Page<Cargo> listar(Pageable pageable, String buscar) throws Exception {
        Map<String, Object> filters = new HashMap<>();
        filters.put("or_codigo", buscar);
        filters.put("or_nombre", buscar);
        filters.put("borrado", 0);

        Map<String, Object> notEqualFilters = new HashMap<>();

        return cargoDAO.findCargosByFiltersPage(filters, notEqualFilters, pageable);
    }

    @Override
    public Cargo registrar(Cargo cargo) throws Exception {
        LocalDateTime fechaActualTime = LocalDateTime.now();
        cargo.setRegDate(fechaActualTime.toLocalDate());
        cargo.setRegDatetime(fechaActualTime);
        cargo.setRegTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        //Oauth inicio
        cargo.setRegUserId(1L);
        //Oauth final

        cargo.setBorrado(Constantes.REGISTRO_NO_BORRADO);
        cargo.setActivo(Constantes.REGISTRO_ACTIVO);

        if(cargo.getCodigo() == null) cargo.setCodigo(Constantes.VOID_STRING);
        if(cargo.getNombre() == null) cargo.setNombre(Constantes.VOID_STRING);
        if(cargo.getSigla() == null) cargo.setSigla(Constantes.VOID_STRING);

        cargo.setCodigo(cargo.getCodigo().trim());
        cargo.setNombre(cargo.getNombre().trim());
        cargo.setSigla(cargo.getSigla().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionRegistro(cargo, resultValidacion);

        if(validacion)
            return this.grabarRegistro(cargo);

        String errorValidacion = "Error de validación Método Registrar Cargo";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }


        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public int modificar(Cargo cargoEdit) throws Exception {

        Cargo cargo = cargoDAO.listarPorId(cargoEdit.getId());

        cargo.setCodigo(cargoEdit.getCodigo());
        cargo.setNombre(cargoEdit.getNombre());
        cargo.setSigla(cargoEdit.getSigla());

        LocalDateTime fechaActualTime = LocalDateTime.now();
        cargo.setUpdDate(fechaActualTime.toLocalDate());
        cargo.setUpdDatetime(fechaActualTime);
        cargo.setUpdTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        //Oauth inicio
        cargo.setUpdUserId(1L);
        //Oauth final

        if(cargo.getCodigo() == null) cargo.setCodigo(Constantes.VOID_STRING);
        if(cargo.getNombre() == null) cargo.setNombre(Constantes.VOID_STRING);
        if(cargo.getSigla() == null) cargo.setSigla(Constantes.VOID_STRING);

        cargo.setCodigo(cargo.getCodigo().trim());
        cargo.setNombre(cargo.getNombre().trim());
        cargo.setSigla(cargo.getSigla().trim());

        Map<String, Object> resultValidacion = new HashMap<String, Object>();

        boolean validacion = this.validacionModificado(cargo, resultValidacion);

        if(validacion)
            return this.grabarRectificar(cargo);

        String errorValidacion = "Error de validación Método Modificar Cargo";

        if(resultValidacion.get("errors") != null){
            List<String> errors =   (List<String>) resultValidacion.get("errors");
            if(errors.size() >0)
                errorValidacion = errors.stream().map(e -> e.concat(". ")).collect(Collectors.joining());
        }


        throw new ValidationServiceException(errorValidacion);
    }

    @Override
    public List<Cargo> listar() throws Exception {
        Map<String, Object> filters = new HashMap<>();
        filters.put("borrado", 0);

        Map<String, Object> notEqualFilters = new HashMap<>();

        return cargoDAO.findCargosByFiltersV2(filters, notEqualFilters);
    }

    @Override
    public Cargo listarPorId(Long id) throws Exception {
        //return cargoDAO.listarPorId(id);

        Map<String, Object> filters = new HashMap<>();
        filters.put("id", id);
        filters.put("borrado", 0);

        Map<String, Object> notEqualFilters = new HashMap<>();

        List<Cargo> cargos = cargoDAO.findCargosByFiltersV2(filters, notEqualFilters);

        if(cargos.size() > 0)
            return cargos.get(0);
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
            String errorValidacion = "Error de validación Método Eliminar Cargo";

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
    public Cargo grabarRegistro(Cargo cargo) throws Exception {
        return cargoDAO.registrar(cargo);
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public int grabarRectificar(Cargo cargo) throws Exception {

        cargoDAO.modificar(cargo);
        return Constantes.CANTIDAD_UNIDAD_INTEGER;
    }

    @Transactional(readOnly=false,rollbackFor=Exception.class)
    @Override
    public void grabarEliminar(Long id) throws Exception {

        Cargo cargoDB = cargoDAO.listarPorId(id);
        LocalDateTime fechaActualTime = LocalDateTime.now();

        cargoDB.setUpdDate(fechaActualTime.toLocalDate());
        cargoDB.setUpdDatetime(fechaActualTime);
        cargoDB.setUpdTimestamp(fechaActualTime.toEpochSecond(ZoneOffset.UTC));

        //Oauth inicio
        cargoDB.setUpdUserId(1L);
        //Oauth final

        cargoDB.setBorrado(Constantes.REGISTRO_BORRADO);

        cargoDAO.modificar(cargoDB);
    }

    @Override
    public boolean validacionRegistro(Cargo cargo, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(cargo.getCodigo() == null || cargo.getCodigo().isEmpty()){
            resultado = false;
            error = "Ingrese el Código del Cargo";
            errors.add(error);
        }

        if(cargo.getNombre() == null || cargo.getNombre().isEmpty()){
            resultado = false;
            error = "Ingrese el Nombre del Cargo";
            errors.add(error);
        }

        Map<String, Object> filters = new HashMap<>();
        filters.put("or_codigo", cargo.getCodigo());
        filters.put("or_nombre", cargo.getNombre());
        filters.put("borrado", 0);

        Map<String, Object> notEqualFilters = new HashMap<>();

        List<Cargo> cargos = cargoDAO.findCargosByFiltersV2(filters, notEqualFilters);

        if(!cargos.isEmpty()){
            resultado = false;
            error = "El código o el Nombre del Cargo ingresado ya se encuentra registrado";
            errors.add(error);
        }

        resultValidacion.put("errors",errors);
        resultValidacion.put("warnings",warnings);

        return resultado;
    }

    @Override
    public boolean validacionModificado(Cargo cargo, Map<String, Object> resultValidacion) throws Exception {
        boolean resultado = true;
        List<String> errors = new ArrayList<String>();
        List<String> warnings = new ArrayList<String>();
        String error;
        String warning;

        if(cargo.getId() == null){
            resultado = false;
            error = "Ingrese el Id del Cargo";
            errors.add(error);
        }

        if(cargo.getCodigo() == null || cargo.getCodigo().isEmpty()){
            resultado = false;
            error = "Ingrese el Código del Cargo";
            errors.add(error);
        }

        if(cargo.getNombre() == null || cargo.getNombre().isEmpty()){
            resultado = false;
            error = "Ingrese el Nombre del Cargo";
            errors.add(error);
        }

        Map<String, Object> filters = new HashMap<>();
        filters.put("or_codigo", cargo.getCodigo());
        filters.put("or_nombre", cargo.getNombre());
        filters.put("borrado", 0);

        Map<String, Object> notEqualFilters = new HashMap<>();
        notEqualFilters.put("id", cargo.getId());

        List<Cargo> cargos = cargoDAO.findCargosByFiltersV2(filters, notEqualFilters);

        if(!cargos.isEmpty()){
            resultado = false;
            error = "El código o el Nombre del Cargo ingresado ya se encuentra registrado";
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
