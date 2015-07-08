--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.4
-- Dumped by pg_dump version 9.4.4
-- Started on 2015-07-08 18:21:11

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 176 (class 3079 OID 11855)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2021 (class 0 OID 0)
-- Dependencies: 176
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 174 (class 1259 OID 24794)
-- Name: student_results; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE student_results (
    system_id integer NOT NULL,
    subject_id integer NOT NULL,
    lab_1 date,
    lab_2 date,
    lab_3 date,
    lab_4 date,
    lab_5 date,
    lab_6 date
);


ALTER TABLE student_results OWNER TO postgres;

--
-- TOC entry 175 (class 1259 OID 24799)
-- Name: subject_table; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE subject_table (
    subject_id integer NOT NULL,
    subject_name character varying(128),
    number_of_labs integer DEFAULT 6 NOT NULL
);


ALTER TABLE subject_table OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 24770)
-- Name: user_data; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE user_data (
    system_id integer NOT NULL,
    login character varying(128) NOT NULL,
    password character varying(128) NOT NULL,
    surname character varying(128) NOT NULL,
    name character varying(128) NOT NULL,
    second_name character varying(128) NOT NULL,
    is_lecturer boolean DEFAULT false NOT NULL,
    group_name character varying(128)
);


ALTER TABLE user_data OWNER TO postgres;

--
-- TOC entry 172 (class 1259 OID 24768)
-- Name: user_data_system_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE user_data_system_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE user_data_system_id_seq OWNER TO postgres;

--
-- TOC entry 2022 (class 0 OID 0)
-- Dependencies: 172
-- Name: user_data_system_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE user_data_system_id_seq OWNED BY user_data.system_id;


--
-- TOC entry 1890 (class 2604 OID 24773)
-- Name: system_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY user_data ALTER COLUMN system_id SET DEFAULT nextval('user_data_system_id_seq'::regclass);


--
-- TOC entry 1900 (class 2606 OID 24798)
-- Name: student_results_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY student_results
    ADD CONSTRAINT student_results_pkey PRIMARY KEY (system_id, subject_id);


--
-- TOC entry 1902 (class 2606 OID 24803)
-- Name: subject_table_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY subject_table
    ADD CONSTRAINT subject_table_pkey PRIMARY KEY (subject_id);


--
-- TOC entry 1894 (class 2606 OID 24783)
-- Name: unique_login; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY user_data
    ADD CONSTRAINT unique_login UNIQUE (login);


--
-- TOC entry 1904 (class 2606 OID 24805)
-- Name: unique_subject_id; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY subject_table
    ADD CONSTRAINT unique_subject_id UNIQUE (subject_id);


--
-- TOC entry 1896 (class 2606 OID 24781)
-- Name: unique_system_id; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY user_data
    ADD CONSTRAINT unique_system_id UNIQUE (system_id);


--
-- TOC entry 1898 (class 2606 OID 24779)
-- Name: user_data_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY user_data
    ADD CONSTRAINT user_data_pkey PRIMARY KEY (system_id);


--
-- TOC entry 2020 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-07-08 18:21:11

--
-- PostgreSQL database dump complete
--

