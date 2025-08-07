package br.ufac.sgcmapi.model;

public enum EStatus {

    CANCELADO,
    AGENDADO,
    CONFIRMADO,
    CHEGADA,
    ATENDIMENTO,
    ENCERRADO;

    public EStatus proximo() {
        var status = this;
        var index = ordinal() + 1;
        if (index > 1 && index < values().length) {
            status = values()[index];
        }
        return status;
    }

}
