package com.clinica.citas.paciente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "paciente")
public class Paciente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String nombre;
    @Column(nullable = false, unique = true) private String documento;
    @Column(nullable = false) private String email;
    private String telefono;
    protected Paciente() { }
    public Paciente(String nombre, String documento, String email, String telefono) {
        this.nombre = nombre; this.documento = documento; this.email = email; this.telefono = telefono;
    }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDocumento() { return documento; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
}
