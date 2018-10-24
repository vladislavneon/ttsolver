data "aws_route53_zone" "sandbox_zone" {
  name = "sandbox.intellij.net."
}

module "vpc_ttsolver" {
  source = "modules/vpc"
  az_count = 2
  resource_prefix = "ttsolver"
}

module "ecs_cluster" {
  source = "modules/ecs_cluster"
  aws_ecs_ec2_instance_type = "t2.small"
  aws_subnet_private_id = "${module.vpc_ttsolver.aws_subnet_private_id}"
  aws_subnet_public_id = "${module.vpc_ttsolver.aws_subnet_public_id}"
  resource_prefix = "ttsolver"
  ec2_instance_security_group_ids = ["${module.internal_security_group.internal_security_group_id}"]
  ec2_public_key_path = "ttsolver_ec2.pub"
  aws_availability_zones_names = "${module.vpc_ttsolver.aws_availability_zones_names}"
  aws_asg_min_size = "1"
  aws_asg_max_size = "1"
}

module "ttsolver_task" {
  source = "modules/ttsolver_ecs_task"
  ecs_cluster_id = "${module.ecs_cluster.ecs_cluster_id}"
  alb_security_group = ["${module.outer_security_groups.outer_security_group_id_80}", "${module.outer_security_groups.outer_security_group_id_443}"]
  resource_prefix = "ttsolver"
  vpc_id = "${module.vpc_ttsolver.vpc_id}"
  public_subnet_id = "${module.vpc_ttsolver.aws_subnet_public_id}"
  aws_region = "${local.region}"
  dns_name = "ttsolver.sandbox.intellij.net"
  zone_id = "${data.aws_route53_zone.sandbox_zone.zone_id}"
}

module "jb_security_groups" {
  source = "modules/security_groups/jb_security_groups"

  resource_prefix = "ttsolver-shared"

  vpc_id = "${module.vpc_ttsolver.vpc_id}"
}

module "internal_security_group" {
  source = "modules/security_groups/internal_security_group"
  resource_prefix = "ttsolver-shared"

  alb_security_group = "${module.jb_security_groups.ipv4_80_jb_security_group_id}"
  vpc_id = "${module.vpc_ttsolver.vpc_id}"
}

module "outer_security_groups" {
  source = "modules/security_groups/outer_security_group"
  resource_prefix = "ttsolver-shared"

  vpc_id = "${module.vpc_ttsolver.vpc_id}"
}

