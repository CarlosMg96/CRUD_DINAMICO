<script setup>
import Peliculas from './components/views/Peliculas.vue';
</script>

<template>
  <div id="app">
    <header>
      <div>
  <b-navbar toggleable="sm" type="light" variant="light">
    <b-navbar-toggle target="nav-text-collapse"></b-navbar-toggle>

    <b-navbar-brand>Películas</b-navbar-brand>

    <b-collapse id="nav-text-collapse" is-nav>
      <b-navbar-nav>
        <b-nav-text>CMGR</b-nav-text>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
</div>
    </header>

    <main>
      <Peliculas />
    </main>
  </div>
</template>

<style >
.div-header{
  background-color: gainsboro;
  border-radius: 4px;
}
</style>
