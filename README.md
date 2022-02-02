# Model Validation [![build-status-server](https://img.shields.io/jenkins/build?jobUrl=https://ci.eclipse.org/emfcloud/job/eclipse-emfcloud/job/model-validation/job/master/)](https://ci.eclipse.org/emfcloud/job/eclipse-emfcloud/job/model-validation/job/master/)

For more information, please visit the [EMF.cloud Website](https://www.eclipse.org/emfcloud/). If you have questions, contact us on our [discussions page](https://github.com/eclipse-emfcloud/emfcloud/discussions) and have a look at our [communication and support options](https://www.eclipse.org/emfcloud/contact/).

This frameworks eases the usage of the [emfcloud-modelserver](https://github.com/eclipse-emfcloud/emfcloud-modelserver) validation capabilities.

This model validation framework supports:

- Triggering a validation of a specific model
- Subscribing to validation result changes (e.g. when the model has changed)
- Retrieving a list of constraints that can be used for input validation
- Keeping a list of constraints that are turned off

## ChangeListener

The ValidationResultChangeListener can be used to update the representation of the ValidationResults everytime the ValidationResult for a specific model has changed.

## InputValidation

The framework contains a Map from ElementId and FeatureId (defined in the metamodel) to an EMFFacetConstraints object. This object contains all of the facets that are defined in the metamodel for the specific feature.

## Filtering Constraints

The framework also keeps a list of all the code/source (defined in the metamodel) pairs, that should be filtered. They can either be turned on/off or can be toggled.

## Maven Repositories [![build-status-server](https://img.shields.io/jenkins/build?jobUrl=https://ci.eclipse.org/emfcloud/job/deploy-emfcloud-model-validation-m2/&label=publish)](https://ci.eclipse.org/emfcloud/job/deploy-emfcloud-model-validation-m2/)

- <i>Snapshots: </i>  https://oss.sonatype.org/content/repositories/snapshots/org/eclipse/emfcloud/validation/

## P2 Update Sites [![build-status-server](https://img.shields.io/jenkins/build?jobUrl=https://ci.eclipse.org/emfcloud/job/deploy-emfcloud-model-validation-p2/&label=publish)](https://ci.eclipse.org/emfcloud/job/deploy-emfcloud-model-validation-p2/)

- <i>Snapshots: </i> https://download.eclipse.org/emfcloud/model-validation/p2/nightly/