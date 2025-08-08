package br.ufac.sgcmapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.ufac.sgcmapi.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    @Query("""
        SELECT u FROM Usuario u
        WHERE :termoBusca IS NULL
        OR u.nomeUsuario LIKE %:termoBusca%
    """)
    List<Usuario> consultar(String termoBusca);

}
