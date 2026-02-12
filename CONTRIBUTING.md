# Contributing Guidelines

## Branching Strategy
- main: stable and production-ready
- develop: integration branch
- feature/<name>: feature branches
- hotfix/<name>: urgent fixes

No direct commits to main.
All changes must go through Pull Requests.

## Commit Convention
We follow Conventional Commits:
- feat: new feature
- fix: bug fix
- docs: documentation
- test: tests
- refactor: code restructuring
- chore: maintenance

Example:
feat(application-service): add status transition validation

## Pull Requests
- Must describe the change clearly.
- Must link to an issue.
- Must pass CI before merging.
