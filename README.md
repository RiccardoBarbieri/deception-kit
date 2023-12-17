# deception-kit
A deception component generator to simplify the creation and deployment of a fake services for the creation of honeypot security mechanism.

### Supported services:
 - OpenID/OAuth2.0 service (Keycloak).

# Getting started

At the moment the tool is only supported on Linux.

## Prerequisites

The only dependency needed to use this tool is [Docker](https://www.docker.com/), follow the [official instructions](https://docs.docker.com/engine/install/) to install docker engine.

### Mockaroo

This project relies on Mockaroo API, an HTTP API that allows users to generate realistic mock data to use in a variety of scenarios without copyright.

To run the tool it is necessary to create an account at [Mockaroo](https://www.mockaroo.com/myaccount) and generate an API key, the key must be placed in an environment variable called `MOCKAROO_API_KEY`.

# Installation

To install the CLI tool `deception-cli` first clone this repository

```bash
git clone https://github.com/RiccardoBarbieri/deception-kit
```

then navigate to

```bash
deception-kit/deception-cli
```

and run `install` bash script, this script will generate the latest build of the project and copy the necessary dependencies in a folder that must be specified as follows

```bash
sudo ./install -d /opt
```

>NOTE: the script must be run as superuser to allow execution permission change on script `deception-cli`

The installation script puts a file named `deception-cli` inside `/usr/bin` folder to be able to access the tool from any location, add the folder to yout `PATH` if it is not already present.

# Usage

To use the tool simply call the `deception-cli` command, without parameters a usage help block will be displayed.

```text
Usage: deception-cli [OPTIONS] [COMMAND]
CLI for DeceptionKit
  -f, --force-restart   Force restart of the deception-core container
  -h, --help            Show this help message and exit.
  -V, --version         Print version information and exit.
Commands:
  generate  Generate a new deception component
```

At the moment the only supported subcommand is `generate`, this command takes two parameters:

1. `-d | --definition` the yaml file that contains the definition of the component you want to generate
2. `-c | --component` the name of the component you want to generate

Both these arguments are required.

## Definition file

The definition file must be written in YAML syntax, below is an example that shows all configurable options for the component "id-provider":

```yaml
component: "id-provider"
specification:
  domain: "localhost.com"
  groups:
    total: 5
    definitions:
      - name: group1
        roles:
          - role2
      - name: group2
        roles:
          - role1
  users:
    total: 10
    groups_per_user: 2
    credentials_per_user: 1
    definitions:
      - username: user1
        firstname: User 1
        lastname: Lastname 1
        email: user1@localhost.com
        enabled: true
        groups:
          - group1
          - group2
        credentials:
          - type: password
            value: password
  clients:
    roles_per_client: 2
    definitions:
      - id: client1
        name: "Client 1"
        description: "Client 1"
        rootUrl: "http://localhost:8080"
        adminUrl: "http://localhost:8080"
        redirectUris:
          - "http://localhost:8080/*"
        baseUrl: "http://localhost:8080"
        protocol: "openid-connect"
        authenticatorType: "client-secret"
        standardFlowEnabled: true,
        implicitFlowEnabled: false,
        directAccessGrantsEnabled: true,
        roles:
          - role1
          - role3
  roles:
    definitions:
      - name: role1
        scope:
          client: client1
      - name: role2
        scope:
          realm: master
      - name: role3
        scope:
          client: client1
```

