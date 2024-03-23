# DesafioDSMovieJacoco

Critérios de correção:
Mínimo para aprovação: 12 de 15
- MovieService.findAll() deve retornar uma página de filmes

- MovieService.findById(id) deve retornar um filme quando o id existir

- MovieService.findById(id) deve lançar ResourceNotFoundException quando o id não existir

- MovieService.insert(dto) deve retornar um filme

- MovieService.update(id, dto) deve retornar um filme quando o id existir

- MovieService.update(id, dto) deve lançar ResourceNotFoundException quando o id não existir

- MovieService.delete(id) deve fazer nada quando o id existir

- MovieService.delete(id) deve lançar ResourceNotFoundException quando o id não existir

- MovieService.delete(id) deve lançar DatabaseException quando o id for dependente

- UserService.authenticated() deve retornar um usuário quando houver usuário logado

- UserService.authenticated() deve lançar UsernameNotFoundException quando não houver usuário logado

- UserService.loadUserByUsername(username) deve retornar um UserDetails quando o username existir

- UserService.loadUserByUsername(username) deve lançar UsernameNotFoundException quando o username não existir

- ScoreService.saveScore(dto) deve retornar os dados do filme quando o id existir

- ScoreService.saveScore(dto) deve lançar ResourceNotFoundException quando o id do filme não existir

Competências avaliadas:
- Testes unitários em projeto Spring Boot com Java

- Implementação de testes unitários com JUnit e Mockito

- Cobertura de código com Jacoco

