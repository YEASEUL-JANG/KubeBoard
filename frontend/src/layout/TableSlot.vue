<template>
  <div class="table-box">
    <h4 class="header"><slot name="header">{{ header }}</slot></h4>
    <slot></slot>
    <slot name="pageSlot"></slot>
  </div>
</template>

<script>
export default {
  props: ['header']
}
</script>

<style>
.header {
  text-align: left;
  margin: 0 0 1rem 1rem;
  font-weight: 600;
}

.table-box {
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.26);
  padding: 2rem;
  margin: 1rem auto 2rem auto;
  min-width: 60rem;
  max-width: 80rem;
  background-color: white;
  overflow-x: auto;
}

th {
  white-space: nowrap;
}

table {
  --bs-table-bg: transparent;
  --bs-table-accent-bg: transparent;
  --bs-table-striped-color: #212529;
  --bs-table-striped-bg: rgba(0, 0, 0, 0.05);
  --bs-table-active-color: #212529;
  --bs-table-active-bg: rgba(0, 0, 0, 0.1);
  --bs-table-hover-color: #212529;
  --bs-table-hover-bg: rgba(0, 0, 0, 0.075);
  width: 100%;
  margin-bottom: 1rem;
  color: #212529;
  vertical-align: top;
  border-color: #dee2e6;
  border-collapse: collapse;
}
table > :not(caption) > * > * {
  padding: 0.5rem 0.5rem;
  background-color: var(--bs-table-bg);
  border-bottom-width: 1px;
  box-shadow: inset 0 0 0 9999px var(--bs-table-accent-bg);
}
table > tbody {
  vertical-align: inherit;
}
table > thead {
  vertical-align: bottom;
}
table > :not(:first-child) {
  border-top: 0px solid currentColor;
}

table > tbody > tr:hover > * {
  --bs-table-accent-bg: var(--bs-table-hover-bg);
  color: var(--bs-table-hover-color);
}
</style>