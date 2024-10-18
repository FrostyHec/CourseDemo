<template>
  {{ $route.fullPath }}
</template>

<script>
  import {loginCall} from '@/api/UserAPI.ts'
  console.log('222')
  await loginCall({
  user_id: 123,
  password: 'string'
})

</script>