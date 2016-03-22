/*
 * Copyright (C) 2015 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.pdsw.samples.textview;


import edu.eci.pdsw.samples.entities.Consulta;
import edu.eci.pdsw.samples.entities.Paciente;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import edu.eci.pdsw.samples.mybatis.mappers.PacienteMapper;

/**
 *
 * @author hcadavid
 */
public class MyBatisExample {

    /**
     * Método que construye una fábrica de sesiones de MyBatis a partir del
     * archivo de configuración ubicado en src/main/resources
     *
     * @return instancia de SQLSessionFactory
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        SqlSessionFactory sqlSessionFactory = null;
        if (sqlSessionFactory == null) {
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream("mybatis-config.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return sqlSessionFactory;
    }

    /**
     * Programa principal de ejempo de uso de MyBATIS
     * @param args
     * @throws SQLException 
     */
    public static void main(String args[]) throws SQLException {
        SqlSessionFactory sessionfact = getSqlSessionFactory();

        SqlSession sqlss = sessionfact.openSession();
        
        PacienteMapper pmap=sqlss.getMapper(PacienteMapper.class);
        
        System.out.println(pmap.loadPacienteById(1019129303, "cc"));
        Paciente p = new Paciente(2103021, "cc", "Camilo", java.sql.Date.valueOf("1997-04-10"));
        pmap.insertPaciente(p);
        Consulta c = new Consulta(java.sql.Date.valueOf("2016-03-22"), "Jairo tiene gripa");
        pmap.insertConsulta(c, 2103021, "cc");
        Paciente p1 = new Paciente(1019093806,"cc","Jairo",java.sql.Date.valueOf("1994-04-10"));
        Consulta c1 = new Consulta(java.sql.Date.valueOf("2016-03-22"), "Jairo se esta mejorando");
        Consulta c2 = new Consulta(java.sql.Date.valueOf("2016-03-23"), "Jairo no se quiere mejorar");
        Consulta c3 = new Consulta(java.sql.Date.valueOf("2016-03-24"), "Excusa medica pa' capar clase");
        p1.getConsultas().add(c1);
        p1.getConsultas().add(c2);
        p1.getConsultas().add(c3);
        registrarNuevoPaciente(pmap, p1);
        sqlss.commit();
        sqlss.close();

    }

    /**
     * Registra un nuevo paciente y sus respectivas consultas (si existiesen).
     * @param pmap mapper a traves del cual se hará la operacion
     * @param p paciente a ser registrado
     */
    public static void registrarNuevoPaciente(PacienteMapper pmap, Paciente p){
        pmap.insertPaciente(p);
        for (Consulta c : p.getConsultas()){
            pmap.insertConsulta(c, p.getId(), p.getTipo_id());
        }
    }
    

}
