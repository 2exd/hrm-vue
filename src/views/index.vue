<template>
  <div class="app-container home">
    <!--? 通讯录  -->
    <AddressBook />

  </div>
</template>

<script lang="ts" name="Index" setup>
import AddressBook from "@/views/hrm/common/addressBook.vue";

const goTarget = (url: string) => {
  window.open(url, '__blank');
};
</script>

<style lang="scss" scoped>
.home {
}
</style>
