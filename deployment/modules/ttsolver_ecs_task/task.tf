resource "aws_ecs_task_definition" "default" {
  family = "${var.resource_prefix}-${var.internal_prefix}-task"
  task_role_arn = "${aws_iam_role.ecs_task_role.arn}"
  network_mode = "host"

  container_definitions = <<DEFINITION
[
    {
      "memory": 500,
      "essential": true,
      "portMappings": [
        {
          "containerPort": 8080,
          "protocol": "tcp"
        }
      ],
      "name": "${var.resource_prefix}-${var.internal_prefix}-front-server-task",
      "ulimits": [
        {
          "softLimit": 50000,
          "hardLimit": 65535,
          "name": "nofile"
        },
        {
          "softLimit": 50000,
          "hardLimit": 65535,
          "name": "nproc"
        }
      ]
      ,
      "image": "${aws_ecr_repository.front_server_repo.repository_url}",
      "logConfiguration" : {
        "logDriver": "awslogs",
        "options": {
            "awslogs-group": "${aws_cloudwatch_log_group.ecs_logs.name}",
            "awslogs-region": "${var.aws_region}",
            "awslogs-stream-prefix": "${var.resource_prefix}-${var.internal_prefix}-stream"
        }
      },
      "cpu": 256
    },
    {
      "memory": 1400,
      "essential": true,
      "portMappings": [
        {
          "containerPort": 5000,
          "protocol": "tcp"
        }
      ],
      "name": "${var.resource_prefix}-${var.internal_prefix}-ml-servertask",
      "ulimits": [
        {
          "softLimit": 50000,
          "hardLimit": 65535,
          "name": "nofile"
        },
        {
          "softLimit": 50000,
          "hardLimit": 65535,
          "name": "nproc"
        }
      ]
      ,
      "image": "${aws_ecr_repository.ml_server_repo.repository_url}",
      "logConfiguration" : {
        "logDriver": "awslogs",
        "options": {
            "awslogs-group": "${aws_cloudwatch_log_group.ecs_logs.name}",
            "awslogs-region": "${var.aws_region}",
            "awslogs-stream-prefix": "${var.resource_prefix}-${var.internal_prefix}-stream"
        }
      },
      "cpu": 256
    }
  ]
DEFINITION
}