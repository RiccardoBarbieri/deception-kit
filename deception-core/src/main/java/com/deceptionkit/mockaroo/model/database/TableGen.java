package com.deceptionkit.mockaroo.model.database;

import com.deceptionkit.mockaroo.MockarooApi;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class TableGen {

    protected String prompt;

    public TableGen(String prompt, int fieldsCount) {
        this.prompt = prompt;
        //direi usa qui la gen types di MockarooApi, ritorna struttura del tipo:
        // anzi no, passa come parametro la struttura del tipo
        // {
        //     "name": "name",
        //     "type": "Name"
        //     "values": [] //optional
        //     "id": 1 //usa per mapping id tipo
        // ID è da rimuovere per richiedere la generazione
        // }
    }

    public String getPrompt() {
        return this.prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

//    public String generateTable() {
//
//    }
}
