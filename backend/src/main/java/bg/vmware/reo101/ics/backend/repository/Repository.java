package bg.vmware.reo101.ics.backend.repository;

import java.util.List;

public interface Repository<T, ID> {
    T get(ID id);

    T delete(ID id);

    List<T> getAll();

    T save(ID id, T t);
}
