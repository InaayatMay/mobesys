package dtos;

public class ProgrammeDto {
    public String value;

    public static ProgrammeDto to(String programme) {
        ProgrammeDto dto = new ProgrammeDto();
        dto.value = programme;
        return dto;
    }
}
