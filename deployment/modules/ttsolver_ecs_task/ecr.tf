resource "aws_ecr_repository" "front_server_repo" {
  name = "front-server-ttsolver"
}

resource "aws_ecr_repository" "ml_server_repo" {
  name = "ml-server-ttsolver"
}