package erp.domain;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Form implements PhotoEntity{
    private String name;
    private String surnames;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    @Pattern(regexp = "[XYZ]?[0-9]{7,8}[A-HJ-NP-TV-Z]", message = "Formato de DNI/NIE inválido")
    private String dni;
    private CustomerType type;
    private String photoPath = "usuario2.png";
    private String parentName;
    @Pattern(regexp = "[0-9]{9}", message = "Formato de teléfono inválido")
    private String phone;
    private List<Activity> activities;
    private String activityNamesString;
    private String[] cursos = {
        "ESO 1",
        "ESO 2",
        "ESO 3",
        "ESO 4",
        "BATX 1",
        "BATX 2",
        "SMIX 1",
        "SMIX 2",
        "DAM 1",
        "DAM 2",
        "DAW 1",
        "DAW 2",
        "ASIX 1",
        "ASIX 2"
    };
    private String course;

    private String [] intereses = {"Lectura", "Viatges", "Cuina", "Esports", "Fotografía", "Música", "Videojocs", "Pel·lícules i sèries", "Activitats a l'aire lluire", "Moda", "Tecnología"};
    private String interests;
}