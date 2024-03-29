--
-- PostgreSQL database dump
--

-- Dumped from database version 15.3
-- Dumped by pg_dump version 15.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: accomplishments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.accomplishments (
    id text NOT NULL,
    user_id text NOT NULL,
    cycle_id text,
    todo_id text,
    name text,
    description text,
    productiveness integer DEFAULT 0 NOT NULL,
    date text NOT NULL,
    "timestamp" text,
    duration bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.accomplishments OWNER TO postgres;

--
-- Name: categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categories (
    id text NOT NULL,
    user_id text NOT NULL,
    category_name text NOT NULL,
    parent_id text
);


ALTER TABLE public.categories OWNER TO postgres;

--
-- Name: cycles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cycles (
    id text NOT NULL,
    user_id text NOT NULL,
    cycle_name text NOT NULL,
    category_id text,
    productiveness integer DEFAULT 0 NOT NULL,
    frequency text NOT NULL,
    archived boolean DEFAULT false NOT NULL
);


ALTER TABLE public.cycles OWNER TO postgres;

--
-- Name: logs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.logs (
    id text NOT NULL,
    user_id text NOT NULL,
    "timestamp" text NOT NULL,
    content text NOT NULL
);


ALTER TABLE public.logs OWNER TO postgres;

--
-- Name: todos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.todos (
    id text NOT NULL,
    user_id text NOT NULL,
    todo_name text NOT NULL,
    category_id text,
    productiveness integer DEFAULT 0 NOT NULL,
    done boolean DEFAULT false NOT NULL,
    due_date text
);


ALTER TABLE public.todos OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id text NOT NULL,
    username text NOT NULL,
    password text NOT NULL,
    creation_time text NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: accomplishments accomplishments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accomplishments
    ADD CONSTRAINT accomplishments_pkey PRIMARY KEY (id);


--
-- Name: categories categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (id);


--
-- Name: cycles cycles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cycles
    ADD CONSTRAINT cycles_pkey PRIMARY KEY (id);


--
-- Name: logs logs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.logs
    ADD CONSTRAINT logs_pkey PRIMARY KEY (id);


--
-- Name: todos todos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.todos
    ADD CONSTRAINT todos_pkey PRIMARY KEY (id);


--
-- Name: users username_index; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT username_index UNIQUE (username);


--
-- Name: CONSTRAINT username_index ON users; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT username_index ON public.users IS 'makes sure the username is unique';


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- Name: accomplishments accomplishments_cycle_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accomplishments
    ADD CONSTRAINT accomplishments_cycle_id_fkey FOREIGN KEY (cycle_id) REFERENCES public.cycles(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: accomplishments accomplishments_todo_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accomplishments
    ADD CONSTRAINT accomplishments_todo_id_fkey FOREIGN KEY (todo_id) REFERENCES public.todos(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: accomplishments accomplishments_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accomplishments
    ADD CONSTRAINT accomplishments_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: categories categories_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_id_fkey FOREIGN KEY (id) REFERENCES public.categories(id) ON UPDATE SET NULL ON DELETE SET NULL NOT VALID;


--
-- Name: categories categories_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: cycles cycles_category_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cycles
    ADD CONSTRAINT cycles_category_id_fkey FOREIGN KEY (category_id) REFERENCES public.categories(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: cycles cycles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cycles
    ADD CONSTRAINT cycles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: logs logs_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.logs
    ADD CONSTRAINT logs_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: todos todos_category_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.todos
    ADD CONSTRAINT todos_category_id_fkey FOREIGN KEY (category_id) REFERENCES public.categories(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: todos todos_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.todos
    ADD CONSTRAINT todos_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- PostgreSQL database dump complete
--

