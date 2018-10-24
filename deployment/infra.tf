provider "aws" {
  region = "eu-west-1"
  profile = "sandbox-jetbrains"
  version = "1.39.0"
}

terraform {
  backend "s3" {
    bucket = "tanvd.tfstate.s3.aws.intellij.net"
    key = "ttsolver/terraform.tfstate"
    profile = "sandbox-jetbrains"
    region = "eu-west-1"
  }
  required_version = "0.11.7"
}

locals {
  region = "eu-west-1"
}